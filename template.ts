import {
  Input,
  prompt,
} from "https://deno.land/x/cliffy@v0.23.0/prompt/mod.ts";

const options = await prompt([
  {
    name: "mod_name",
    message: "Enter mod name:",
    type: Input,
  },
  {
    name: "mod_id",
    message: "Enter mod ID:",
    type: Input,
  },
  {
    name: "main_class_name",
    message: "Enter main class name:",
    type: Input,
  },
  {
    name: "maven_group",
    message: "Enter maven group:",
    type: Input,
    default: "io.github.jamalam360",
  },
  {
    name: "description",
    message: "Enter description:",
    type: Input,
  },
  {
    name: "author",
    message: "Enter author:",
    type: Input,
    default: "Jamalam",
  },
  {
    name: "github_user",
    message: "Enter github user/organisation:",
    type: Input,
    default: "JamCoreModding",
  },
  {
    name: "github_repo",
    message: "Enter github repo:",
    type: Input,
  }
]);

const mainPackage = `${options.maven_group}/${
  options.mod_id!.split("_").join("/").split("-").join("/")
}`.replaceAll(".", "/");
const mainClass =
  `${Deno.cwd()}/src/main/java/${mainPackage.replaceAll(".", "/")}/${options.main_class_name}.java`;

await transformCi();
await transformMainPackage();
await transformMainClass();
await transformAssetsDirectory();
await transformFabricModJson();
await transformMixinsJson();
await transformReadme();
await transformLicense();

async function transformCi() {
  const ci = `
name: Update Licenses and Build
on: push

jobs:
  build:
    strategy:
      matrix:
        java: [
            17
        ]
        os: [ ubuntu-20.04 ]
    runs-on: \${{ matrix.os }}
    steps:
      - name: Checkout Repository
        uses: actions/checkout@v2
        with:
          persist-credentials: false
          fetch-depth: 0

      - name: Get Branch Name
        id: branch-name
        uses: tj-actions/branch-names@v5

      - name: Validate Gradle Wrapper
        uses: gradle/wrapper-validation-action@v1

      - name: Setup JDK \${{ matrix.java }}
        uses: actions/setup-java@v1
        with:
          java-version: \${{ matrix.java }}

      - name: Make Gradle Wrapper Executable
        if: \${{ runner.os != 'Windows' }}
        run: chmod +x ./gradlew

      - name: Update Licenses
        continue-on-error: true
        run: |
          ./gradlew updateLicenses
          git config --local user.email "41898282+github-actions[bot]@users.noreply.github.com"
          git config --local user.name "github-actions"
          git pull origin \${{ steps.branch-name.outputs.current_branch }}
          git commit -m "Update Licenses [bot]" -a

      - name: Push License Changes
        uses: ad-m/github-push-action@master
        with:
          github_token: \${{ secrets.GITHUB_TOKEN }}
          branch: \${{ github.ref }}
          
      - name: Build
        run: ./gradlew build

      - name: Capture Build Artifacts
        uses: actions/upload-artifact@v2
        with:
          name: Artifacts
          path: build/libs/
    `;

  await Deno.writeTextFile(`${Deno.cwd()}/.github/workflows/build.yml`, ci);
  await Deno.remove(`${Deno.cwd()}/.github/workflows/.exists`)
}

async function transformMainPackage() {
  await Deno.mkdir(`${Deno.cwd()}/src/main/java/${mainPackage.replaceAll(".", "/")}`, { recursive: true });

  await Deno.rename(
    `${Deno.cwd()}/src/main/java/io/github/jamalam360/templatemod`,
    `${Deno.cwd()}/src/main/java/${mainPackage.replaceAll(".", "/")}`,
  );
}

async function transformMainClass() {
  await Deno.rename(
    `${Deno.cwd()}/src/main/java/${mainPackage}/TemplateModInit.java`,
    mainClass,
  );

  const content = await Deno.readTextFile(mainClass);

  await Deno.writeTextFile(
    mainClass,
    content.replaceAll("TemplateModInit", options.main_class_name!)
      .replaceAll("templatemod", options.mod_id!)
      .replaceAll("Template Mod", options.mod_name!)
      .replaceAll("io.github.jamalam360.templatemod", mainPackage),
  );
}

async function transformAssetsDirectory() {
  await Deno.rename(
    `${Deno.cwd()}/src/main/resources/assets/templatemod`,
    `${Deno.cwd()}/src/main/resources/assets/${options.mod_id}`,
  );
}

async function transformFabricModJson() {
  const fmj = `./src/main/resources/fabric.mod.json`;
  const fmjContent = await Deno.readTextFile(fmj);
  await Deno.writeTextFile(
    fmj,
    fmjContent
      .replaceAll(
        "io.github.jamalam360.templatemod.TemplateModInit",
        mainClass.substring(`${Deno.cwd()}/src/main/java/`.length).replaceAll("/", "."),
      )
      .replaceAll("templatemod", options.mod_id!)
      .replaceAll("Template Mod", options.mod_name!)
      .replaceAll("A Fabric mod template", options.description!)
      .replaceAll("Jamalam", options.author!)
      .replaceAll("JamCoreModding", options.github_user!)
      .replaceAll("FabricTemplateMod", options.github_repo!),
  );
}

async function transformMixinsJson() {
    const mixins = `${Deno.cwd()}/src/main/resources/${options.mod_id}.mixins.json`;
    await Deno.rename(`${Deno.cwd()}/src/main/resources/templatemod.mixins.json`, mixins);
    const mixinsContent = await Deno.readTextFile(mixins);
    await Deno.writeTextFile(
        mixins,
        mixinsContent.replaceAll("io.github.jamalam360.templatemod", mainPackage.replaceAll("/", "."))
    );
}

async function transformReadme() {
    await Deno.writeTextFile(
        "./README.md",
        `
# ${options.mod_name}

${options.description}
        `
    );
}

async function transformLicense() {
    await Deno.writeTextFile(
        "./LICENSE",
        await (await Deno.readTextFile("./LICENSE")).replaceAll("Jamalam", options.author!)
    );
}
