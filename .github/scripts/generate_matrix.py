import yaml
import json

with open("languages.yaml") as f:
    languages = yaml.safe_load(f)["languages"]

with open("addons.yaml") as f:
    addons = yaml.safe_load(f)["addons"]

matrix = []

for lang, versions in languages.items():
    for version in versions:
        base_image = f"{lang}:{version}"
        packages = " ".join(addons.get(lang, []))

        matrix.append({
            "language": lang,
            "version": version,
            "base_image": base_image,
            "packages": packages
        })

print(json.dumps({"include": matrix}, indent=2))
