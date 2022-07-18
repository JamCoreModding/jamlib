## Changelog

**BREAKING CHANGE**: Artifact specifier has been changed from `jam-lib` to `jamlib`.

### Features

- Add the annotation `@BlockItemFactory` to allow registry classes to construct their own block items.
- Add the annotation `@WithoutBlockItem` to allow blocks to opt out of `BlockItem` construction.
- Add a simple networking API that reduces the amount of boilerplate code needed to send packets.

### Fixes

- `ScreenHandlerType`s can now be registered.
- Fix an incorrect method call in the registry.

Closed issues: None.

[Full Changelog](https://github.com/JamCoreModding/JamLib/compare/0.1.0...0.2.0)
