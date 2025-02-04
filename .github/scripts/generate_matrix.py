import yaml
import json
import os

# קריאת קובצי ה-YAML
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

# שמירת המטריצה לפלט JSON תקין
matrix_json = json.dumps({"include": matrix})

with open(os.getenv('GITHUB_ENV'), 'a') as env_file:
    env_file.write(f'MATRIX={matrix_json}\n')

# הדפסת ה-JSON כדי לוודא שהפורמט תקין
print(matrix_json)
