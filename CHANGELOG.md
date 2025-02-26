- Ensure inherited fields are present in config GUIs (closes #13).
- When both a mod ID and config file name are specified, the config file is now saved under
  `config/{mod id}/{config name}.json5` (closes #12).
    - This should not be a breaking change as I am not aware of any mods registering multiple configs currently.
- Switch to fabric-api mod ID in dependencies block (closes #10).
- Enable split source sets (closes #14).
- Identify config managers by `(MOD_ID, CONFIG_NAME)` rather than by just `(CONFIG_NAME)` (closes #15).
- Allow `List<?>` config fields (closes #11).
- The reset button next to each config field now resets to the default value, rather than the value the field had when
  the config screen was opened.