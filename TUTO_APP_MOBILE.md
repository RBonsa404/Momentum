# 📱 Guide Complet : Transformer Momentum en Application Mobile (Android & iOS)

Ce guide pas-à-pas très détaillé explique comment transformer votre projet **Angular 19.2** actuel en une **application mobile native Android et iOS** complète grâce à **Capacitor**, et comment la connecter à votre backend Spring Boot déployé sur **Railway.app**.

---

## 🏗️ Architecture Globale Mobile

```
+-------------------------------------------------------+
|             📱 APPLICATION MOBILE NATIVE               |
|  (Enveloppe Capacitor + Angular 19.2 HTML/CSS/TS)     |
+-------------------------------------------------------+
                           |
            Requêtes HTTPS (JSON / REST API)
                           v
+-------------------------------------------------------+
|             ☁️ BACKEND CLOUD (Railway.app)            |
|   https://momentum-production-f9bd.up.railway.app     |
|   (Spring Boot 4.1.1 + PostgreSQL 16 + JWT)          |
+-------------------------------------------------------+
```

---

## 📋 PRÉREQUIS

1. **Node.js** (v18 ou supérieur) & **npm** installés.
2. **Android Studio** (gratuit, requis pour compiler l'APK/AAB Android et exécuter les émulateurs).
3. *(Optionnel pour iOS)* **macOS** + **Xcode** (requis seulement pour générer la version iPhone / iOS).
4. Votre backend **Spring Boot** fonctionnel et déployé sur **Railway** (`https://momentum-production-f9bd.up.railway.app`).

---

## ⚡ ÉTAPE 1 : Adapter Angular pour l'environnement Mobile

Lorsque l'application tourne dans un téléphone mobile sous forme de paquet natif, les requêtes HTTP relatives comme `/api/auth/login` cherchent le serveur sur `http://localhost/api/...` du téléphone, ce qui échoue.

Nous devons configurer Angular pour pointer vers l'URL absolue de votre backend Railway en production.

### 1.1 Créer l'environnement de production mobile
Dans `frontend/src/environments/environment.prod.ts` (ou dans `environment.ts`) :

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://momentum-production-f9bd.up.railway.app'
};
```

### 1.2 Ajouter un Intercepteur d'API URL dans Angular
Créez un intercepteur dans `frontend/src/app/core/api-url.interceptor.ts` :

```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const apiUrlInterceptor: HttpInterceptorFn = (req, next) => {
  // Si la requête commence par /api, on préfixe avec l'URL backend Railway en production
  if (req.url.startsWith('/api') && environment.apiUrl) {
    const apiReq = req.clone({ url: `${environment.apiUrl}${req.url}` });
    return next(apiReq);
  }
  return next(req);
};
```

Enregistrez cet intercepteur dans `frontend/src/app/app.config.ts` :

```typescript
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { apiUrlInterceptor } from './core/api-url.interceptor';
import { authInterceptor } from './core/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([apiUrlInterceptor, authInterceptor])
    )
  ]
};
```

---

## 🔌 ÉTAPE 2 : Installer et Initialiser Capacitor

Placez-vous dans le dossier `frontend` de votre projet :

```bash
cd frontend

# 1. Installer le cœur de Capacitor et le CLI
npm install @capacitor/core
npm install -D @capacitor/cli

# 2. Initialiser Capacitor dans le projet
npx cap init Momentum com.momentum.app --web-dir dist/momentum-web/browser
```

*(Note : `--web-dir dist/momentum-web/browser` correspond au dossier de sortie de build d'Angular 19).*

---

## 🤖 ÉTAPE 3 : Configurer et Générer l'application Android

### 3.1 Installer le module Android
```bash
# Dans /frontend
npm install @capacitor/android
npx cap add android
```

Un dossier `frontend/android` contenant le projet natif Android a été généré automatiquement !

### 3.2 Compiler Angular et Synchroniser avec Android
Chaque fois que vous modifiez le code Angular, exécutez ces deux commandes :

```bash
# 1. Compilation du code Angular
npm run build

# 2. Copie du code compilé dans le projet Android
npx cap sync
```

### 3.3 Ouvrir le projet dans Android Studio
```bash
npx cap open android
```

Android Studio s'ouvre avec le projet prêt à être exécuté.

---

## 📱 ÉTAPE 4 : Tester sur Émulateur & Appareil Physique

### Test sur Émulateur Android (dans Android Studio) :
1. Dans Android Studio, cliquez sur le menu **Device Manager** en haut à droite.
2. Créez un appareil virtuel (ex: *Pixel 7 - Android 14*).
3. Cliquez sur le bouton vert ▶️ **Run 'app'** pour lancer l'application.

### Test sur Appareil Physique Android (Smartphone en USB) :
1. Sur votre smartphone Android, activez les **Options pour développeurs** ➔ **Débogage USB**.
2. Branchez le téléphone à votre PC via USB.
3. Dans Android Studio, sélectionnez votre téléphone dans la liste déroulante des appareils et cliquez sur ▶️ **Run**.

---

## 🍎 ÉTAPE 5 : Configurer et Générer l'application iOS (sur Mac)

*(Cette étape nécessite un Mac avec Xcode installé)*.

```bash
cd frontend

# 1. Ajouter la plateforme iOS
npm install @capacitor/ios
npx cap add ios

# 2. Synchroniser le projet
npm run build
npx cap sync

# 3. Ouvrir Xcode
npx cap open ios
```

Dans Xcode :
1. Sélectionnez l'équipe de développement (**Signing & Capabilities** ➔ **Team**).
2. Choisissez un simulateur (ex: *iPhone 15 Pro*) et appuyez sur ▶️ **Run**.

---

## ⚙️ ÉTAPE 6 : Ajouter des Fonctionnalités Natives (Bonus Mobile)

Capacitor fournit des plugins très faciles à ajouter pour améliorer l'expérience mobile :

### 1. Retours Haptiques (Vibrations lors d'une tâche accomplie) :
```bash
npm install @capacitor/haptics
```
Utilisation dans votre composant Angular :
```typescript
import { Haptics, ImpactStyle } from '@capacitor/haptics';

async onTaskCompleted() {
  await Haptics.impact({ style: ImpactStyle.Medium });
}
```

### 2. Personaliser la Barre de Statut (Couleur / Dark Mode) :
```bash
npm install @capacitor/status-bar
```
```typescript
import { StatusBar, Style } from '@capacitor/status-bar';

StatusBar.setStyle({ style: Style.Dark });
StatusBar.setBackgroundColor({ color: '#1E1E2E' });
```

### 3. Splash Screen (Écran de chargement au démarrage) :
```bash
npm install @capacitor/splash-screen
```

---

## 📦 ÉTAPE 7 : Déploiement & Publication de l'Application

### 🤖 Pour Android (Google Play Store)

1. **Générer la Clé de Signature (Keystore)** :
   Dans Android Studio ➔ Menu **Build** ➔ **Generate Signed Bundle / APK**.
2. **Choisir Android App Bundle (.aab)** :
   Sélectionnez **Android App Bundle** (requis par Google).
3. **Créer une clé sécurisée** :
   Créez une clé de signature et conservez votre mot de passe en lieu sûr.
4. **Publier sur Google Play Console** :
   - Créez un compte sur [Google Play Console](https://play.google.com/console) (frais uniques de 25$).
   - Créez une nouvelle application, remplissez la fiche du store (Captures d'écran, description).
   - Téléversez votre fichier `.aab` dans l'onglet **Production** ou **Test Interne**.

#### Distribution Directe de l'APK (Sans passer par le Play Store) :
Dans Android Studio ➔ **Build** ➔ **Build APK(s)**. Vous obtiendrez un fichier `app-release.apk` que n'importe qui peut installer directement sur son téléphone Android !

---

### 🍎 Pour iOS (Apple App Store)

1. **Compte Développeur Apple** :
   Inscrivez-vous sur le [Apple Developer Program](https://developer.apple.com/) (99$/an).
2. **Archiver l'Application** :
   Dans Xcode ➔ Menu **Product** ➔ **Archive**.
3. **Téléverser vers TestFlight / App Store Connect** :
   Cliquez sur **Distribute App** ➔ **TestFlight & App Store**.
4. Validez l'application sur [App Store Connect](https://appstoreconnect.apple.com).

---

## 🔄 RÉCAPITULATIF DU WORKFLOW DE DÉVELOPPEMENT MOBILE

Chaque fois que vous apportez des modifications à votre code Angular :

```bash
# 1. Compiler Angular
npm run build

# 2. Synchroniser les modifications vers Android/iOS
npx cap sync

# 3. Tester dans Android Studio / Xcode
npx cap open android
```

Votre application mobile **Momentum** fonctionnera de façon ultra-fluide avec votre backend **Railway.app** ! 🚀
