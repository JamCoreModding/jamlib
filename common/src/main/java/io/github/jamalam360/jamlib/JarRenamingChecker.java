package io.github.jamalam360.jamlib;

import com.google.common.io.Files;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

// 9minecraft and related sites often rename jars.
// This class checks the manifest of a given class to see if it has been renamed.
// For this to work, the jar's manifest file has to have a "JamLib-File-Name" attribute.
@ApiStatus.Internal
public class JarRenamingChecker {
	private final List<String> suspiciousJars = new ArrayList<>();
	private final File knownSuspiciousJarsFile = new File("config/jamlib/known_suspicious_jars.txt");

	public JarRenamingChecker() {
		try {
			if (!this.knownSuspiciousJarsFile.exists()) {
				this.knownSuspiciousJarsFile.getParentFile().mkdirs();
				this.knownSuspiciousJarsFile.createNewFile();
			}
		} catch (IOException ignored) {
			JamLib.LOGGER.warn("Failed to create suspicious jar file list, this may cause annoying notifications.");
		}
	}

	public List<String> getSuspiciousJarsToNotifyAbout() {
		final List<String> knownSuspiciousJars = new ArrayList<>();

		try {
			knownSuspiciousJars.addAll(Files.readLines(this.knownSuspiciousJarsFile, Charset.defaultCharset()));
		} catch (IOException ignored) {
			JamLib.LOGGER.warn("Failed to read known suspicious jar file list, this may cause annoying notifications.");
		}

		return this.suspiciousJars.stream().filter(jar -> !knownSuspiciousJars.contains(jar)).toList();
	}

	public void afterNotify() {
		try {
			Files.asCharSink(this.knownSuspiciousJarsFile, Charset.defaultCharset()).write(String.join("\n", this.suspiciousJars));
		} catch (IOException ignored) {
			JamLib.LOGGER.warn("Failed to write known suspicious jar file list, this may cause annoying notifications.");
		}
	}

	public void checkJar(Class<?> clazz) {
		Manifest manifest = readManifestFromClass(clazz);

		if (manifest == null) {
			return;
		}

		String jarName = manifest.getMainAttributes().getValue("JamLib-File-Name");

		if (jarName == null) {
			return;
		}

		String[] split = getActualJarPath(clazz).split("/");

		if (!jarName.equals(split[split.length - 1])) {
			split = getActualJarPath(clazz).split("/");
			suspiciousJars.add(split[split.length - 1] + " (should be " + jarName + ")");
		}
	}

	@Nullable
	private static Manifest readManifestFromClass(Class<?> clazz) {
		try {
			String path = getActualJarPath(clazz);
			String manifestPath = "jar:file:" + path + "!/META-INF/MANIFEST.MF";
			return new Manifest(new URL(manifestPath).openStream());
		} catch (Exception ignored) {
			// Silently fail, since this functionality is non-critical.
		}

		return null;
	}

	private static String getActualJarPath(Class<?> clazz) {
		String classFilePath = clazz.getName().replace('.', '/') + ".class";
		String path = clazz.getResource("/" + classFilePath).toString();
		if (path.startsWith("jar:file:")) {
			path = path.substring("jar:file:".length(), path.indexOf("!"));
		}

		return path;
	}
}
