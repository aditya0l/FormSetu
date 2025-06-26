# FormSetu

FormSetu is a modern Android app (Jetpack Compose, Kotlin, iText 7) for filling, validating, and exporting official Indian government forms (PAN, Passport, Aadhaar Update) with AI guidance, OCR autofill, and print-ready PDF export.

## Features

- **Dynamic Forms:** Renders PAN, Passport, and Aadhaar Update forms from JSON schemas, matching official field lists (English/Hindi labels, required/optional, dropdowns, etc.).
- **AI Guidance:** Contextual hints and validation for each field.
- **OCR Autofill:** Use your camera to scan and autofill form fields.
- **PDF Export:** Overlays filled data on official government PDF templates for print-ready submission.
- **Submission History:** Saves all submissions locally (Room DB), with history, search, and re-export.
- **Modern UI:** Material3 design, light/dark mode, animated errors, and responsive layouts.
- **Multi-language:** English and Hindi support for all form fields.

## Screenshots

<!-- Add screenshots here if available -->

## Getting Started

### Prerequisites

- Android Studio (Giraffe or newer recommended)
- Android device or emulator (API 24+)
- [iText 7](https://itextpdf.com/) (already included in dependencies)

### Build & Run

1. **Clone the repo:**
   ```sh
   git clone https://github.com/aditya0l/FormSetu.git
   cd FormSetu
   ```

2. **Open in Android Studio:**  
   `File > Open > Select the FormSetu directory`

3. **Build & Run:**  
   Click the Run button or use `Shift+F10`.

### Project Structure

- `app/src/main/assets/forms/` — JSON schemas for each form
- `app/src/main/assets/templates/` — Official PDF templates
- `app/src/main/java/com/example/formsetu/` — App source code (UI, data, utils, viewmodels)
- `app/src/main/res/` — Resources (drawables, strings, themes)

## How It Works

1. **Select a Form:** Choose PAN, Passport, or Aadhaar Update from the Home screen.
2. **Fill the Form:** Fields are rendered dynamically, with AI hints and OCR autofill.
3. **Save & Export:** Submissions are saved locally and can be exported to official PDF templates.
4. **View History:** Access, search, and re-export previous submissions.

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](LICENSE) (or specify your license)

## Acknowledgements

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [iText 7](https://itextpdf.com/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- Official government forms and templates

---

**Questions?**  
Open an issue or contact [aditya0l](https://github.com/aditya0l).