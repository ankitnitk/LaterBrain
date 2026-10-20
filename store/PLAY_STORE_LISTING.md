# Later Brain — Play Store listing kit

Everything needed to fill in Google Play Console, in the order Play Console asks for it.
Copy and paste the text in the boxes. Character counts were checked against Google's limits.

---

## 1. Create the app

Play Console → **Create app**

| Field | Answer |
|---|---|
| App name | `Later Brain` |
| Default language | English (United States) – en-US |
| App or game | App |
| Free or paid | **Free** (this can never be changed to paid later; future extras would be in-app purchases) |
| Declarations | Tick both boxes (Developer Programme Policies, US export laws) |

---

## 2. Store listing (Grow users → Store presence → Main store listing)

### App name (max 30) — 27 characters
```
Later Brain: Save for Later
```
*Alternative if you prefer the plain name:* `Later Brain` (11)

### Short description (max 80) — 73 characters
```
Save links, places and ideas. Get them back at the moment they're useful.
```

### Full description (max 4000)
```
You saved it for a reason. Later Brain reminds you when that reason arrives.

A restaurant a friend mentioned. An article about sleep. A recipe for the weekend. An idea you had in the shower. You save them, and then they disappear into a list you never open again.

Later Brain is different. It isn't a bookmark list and it isn't a read-later pile. It does the remembering for you, and brings each thing back at the moment it's actually useful:

• Recipes on a Saturday morning
• That sleep article in the evening
• The restaurant when you have a free night

━━━━━━━━━━

SAVE IN TWO SECONDS
Tap Share in Chrome, Google Maps, YouTube or any app and pick Later Brain. The title and link are filled in for you. Or tap Quick add and type a thought.

ONE LIGHT QUESTION
"When should I remind you?" Tonight, This weekend, Next time I'm free, or Surprise me. That's it.

SORTED FOR YOU
Later Brain guesses whether it's a restaurant, recipe, article, video, book, place, trip, shopping or an idea. Change it with one tap if it guesses wrong.

GENTLE, NEVER NAGGING
• A maximum of 2 reminders a day (you choose)
• Quiet hours, 10pm to 7am by default
• Every reminder has three buttons: Done, Later, Never again
• The same thing never comes back twice in three days, unless you asked it to

CALM BY DESIGN
Opening Later Brain feels like opening a drawer, not scrolling a feed. One "Maybe today" pick, and everything else waiting quietly. Light and dark mode.

━━━━━━━━━━

PRIVATE. REALLY.
• Everything stays on your phone
• No account, no sign-up, no login
• No ads, no analytics, no tracking
• The app doesn't even have internet permission, so your data physically can't leave your phone
• Your saved items aren't copied to cloud backup

Free, with no ads.
```

### Graphics

| Asset | File | Notes |
|---|---|---|
| App icon (512×512) | `store/graphics/icon-512.png` | Google rounds the corners itself |
| Feature graphic (1024×500) | `store/graphics/feature-graphic-1024x500.png` | |
| Phone screenshots (2–8) | take them on your phone, see section 6 | |
| Tablet screenshots | optional, skip | only needed to be featured on tablets |
| Video | optional, skip | |

### Category and contact (Store settings)

| Field | Answer |
|---|---|
| App category | **Productivity** |
| Tags | Productivity, Note taking, To-do list *(pick from Google's list; choose up to 5 that fit)* |
| Email | `ankitnitk@gmail.com` (shown publicly on the store page) |
| Website | `https://ankitnitk.github.io/LaterBrain/` |
| Phone | leave empty |
| External marketing | your choice |

---

## 3. App content (Policy → App content)

Answer each section like this.

### Privacy policy
```
https://ankitnitk.github.io/LaterBrain/privacy-policy.html
```

### Ads
**No**, my app does not contain ads.

### App access
**All functionality is available without any access restrictions.** (No login.)

### Content rating
Start questionnaire → email `ankitnitk@gmail.com` → category **"All other app types"** (utility, productivity, communication…).
Answer **No** to every question: no violence, no sexual content, no swearing, no drugs, no gambling, no user-to-user chat or sharing, no sharing of location with other users, no purchases of digital goods, no web browser.
Expected result: **Everyone / PEGI 3 / IARC 3+.**

### Target audience and content
- Target age group: **18 and over** only.
  *(Choosing any under-13 age brings in the stricter Families policy. 18+ keeps it simple and doesn't stop anyone from installing.)*
- Could the app unintentionally appeal to children? **No.**

### News app
**No.**

### Data safety
This is the form behind the "No data collected" badge on the store page.

| Question | Answer |
|---|---|
| Does your app collect or share any of the required user data types? | **No** |
| *(If asked)* Is all user data encrypted in transit? | Not applicable, no data is sent |
| *(If asked)* Do you provide a way for users to request deletion? | Not applicable, nothing is collected |

The store will then show **"No data collected"** and **"No data shared with third parties"**.

Why this is true: Google counts data as *collected* only if it leaves the device. Later Brain has no internet permission, so nothing can leave.

### Advertising ID
**No**, my app does not use an advertising ID.

### Government app
**No.**

### Financial features
**My app doesn't provide any financial features.**

### Health
**My app does not have any health features.**

### Permissions declarations (later)
- No special declarations are needed for v1.0.
- v1.1 (place reminders) will need the **background location** declaration, with a short demo video and the in-app explanation screen. We'll do that then.

---

## 4. Closed testing (needed before production)

New personal developer accounts must run a closed test with **at least 12 testers opted in for 14 days in a row** before they can apply for production.

1. **Testing → Closed testing → Create track** (or use the default "Alpha" track).
2. **Testers tab** → Create email list, e.g. "Later Brain testers". Add at least 12 Gmail addresses (aim for 15–20, since some people forget to opt in).
3. **Countries/regions** → add Kenya (plus any countries your testers are in).
4. **Create new release** → upload the AAB:
   - GitHub → Actions → latest green "Build Later Brain" run → download the **LaterBrain-AAB** artifact → unzip → `LaterBrain-v1.0.0.aab`.
   - On the first upload, accept **Play App Signing** (Google keeps the final signing key; ours is the "upload key").
   - Release name: `1.0.0 (1)`.
   - Release notes:
     ```
     First test build. Save links, places and ideas from any app's Share menu, or type a quick note. Reminders are coming in the next build.
     ```
5. **Review and roll out** → send for review. The first review can take a few days.
6. When approved, copy the **opt-in link** from the Testers tab and send it to your testers. Each tester must:
   1. open the link on their phone and tap **Become a tester**;
   2. install the app from the Play Store;
   3. **keep it installed for the whole 14 days.**

> Tip: the 14-day clock only counts days with 12 or more testers opted in. Ask testers to actually use it (save a few things, tap a reminder). Google asks about tester engagement when you apply for production.

### Message you can send to testers
```
Hi! I've built a small Android app called Later Brain. You save links, places and ideas from any app's Share menu, and it reminds you of them at the moment they're useful. It's free, has no ads, and nothing leaves your phone.

Could you help me test it? Google needs 12 testers for 14 days before I can publish it.

1. Open this link on your Android phone and tap "Become a tester": <OPT-IN LINK>
2. Install Later Brain from the Play Store
3. Keep it installed for 2 weeks, and save a few things you'd like to come back to

Thank you!
```

---

## 5. Before you press "Apply for production"

- [ ] The full description only promises features that are in the build (calendar and reminders arrive in Phases 2–3; update the text if anything is cut)
- [ ] Fresh screenshots that show reminders and settings
- [ ] Privacy policy "Last updated" date is current
- [ ] 14 days of 12+ opted-in testers finished

---

## 6. Screenshots: what to capture on your phone

Requirements: PNG or JPEG, between 320 and 3840 pixels on each side, portrait 9:16 is ideal. Most modern phones already meet this. Press **Power + Volume down** to capture.

**First add some realistic sample items** so the screens don't look empty:

| Share or type | Reminder |
|---|---|
| A Google Maps place for a Nairobi restaurant (share from Maps) | Next time I'm free |
| A recipe page from Chrome, e.g. chapati or pancakes | Weekend |
| An article about sleep from Chrome | Tonight |
| A YouTube video | Surprise me |
| Quick add: "Ask Wanjiru about the hiking trip to Ngong Hills" | Weekend |

**Then capture these 4–5 screens:**
1. **Home**: the gold "Maybe today" card with the list below it.
2. **Share sheet**: Chrome's Share menu with "Save to Later Brain" visible.
3. **Save screen**: opened from a share, with "When should I remind you?" chips showing.
4. **Item detail**: one item, showing the Open link and Mark as done buttons.
5. **Dark mode**: the home screen again with your phone in dark mode.

Copy the images into `store/screenshots/` on the PC (for example through USB or Google Drive). I can then add a caption band above each one ("Save from any app", "Back at the right moment"…) to make the listing look more polished.

Once Phase 2 is done, replace or add a screenshot of a **reminder notification** with its Done / Later / Never again buttons. That's the most persuasive picture for this app.
