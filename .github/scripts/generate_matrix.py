import yaml
import json

# קריאת קובצי YAML
with open("languages.yaml") as f:
    languages = yaml.safe_load(f)["languages"]

with open("addons.yaml") as f:
    addons = yaml.safe_load(f)["addons"]

# שמירת תלויות בסיסיות (כגון curl, git, wget)
base_packages = " ".join(addons.get("base_packages", []))

matrix = []

for lang, versions in languages.items():
    for version in versions:
        base_image = f"openjdk:{version}-jdk-bullseye" if lang == "java" else f"{lang}:{version}"
        lang_packages = " ".join(addons.get(lang, []))

        matrix.append({
            "language": lang,
            "version": version,
            "base_image": base_image,
            "base_packages": base_packages,  # חבילות מערכת הפעלה
            "packages": lang_packages        # חבילות ספציפיות לשפה
        })

# כתיבת המטריצה כקובץ JSON קריא
with open("matrix.json", "w") as f:
    json.dump({"include": matrix}, f, indent=2)

print(json.dumps({"include": matrix}, indent=2))  # הדפסה לבדיקה
