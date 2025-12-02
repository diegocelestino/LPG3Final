# Firebase Setup Instructions

## Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select an existing project
3. Follow the setup wizard

## Step 2: Enable Realtime Database

1. In your Firebase project, go to "Build" → "Realtime Database"
2. Click "Create Database"
3. Choose a location (e.g., us-central1)
4. Start in **test mode** for development (you can secure it later)

## Step 3: Get Service Account Key

1. Go to Project Settings (gear icon) → "Service accounts"
2. Click "Generate new private key"
3. Download the JSON file
4. Save it in your project root as `serviceAccountKey.json`
5. **IMPORTANT**: Add `serviceAccountKey.json` to `.gitignore`

## Step 4: Configure Your Application

1. Open `src/main/resources/firebase-config.properties`
2. Update the following:
   - `firebase.serviceAccountPath`: Path to your JSON file (e.g., `./serviceAccountKey.json`)
   - `firebase.databaseUrl`: Your database URL (found in Realtime Database settings)

Example:
```properties
firebase.serviceAccountPath=./serviceAccountKey.json
firebase.databaseUrl=https://your-project-id-default-rtdb.firebaseio.com
```

## Step 5: Security Rules (Optional for Production)

In Firebase Console → Realtime Database → Rules, update to:

```json
{
  "rules": {
    "clients": {
      ".read": true,
      ".write": true
    }
  }
}
```

For production, implement proper authentication and authorization rules.

## Step 6: Test Connection

Run your application and try to save a client. Check Firebase Console to see if data appears.

## Troubleshooting

- **Authentication error**: Check if service account JSON path is correct
- **Permission denied**: Verify database rules allow read/write
- **Connection timeout**: Check database URL format
