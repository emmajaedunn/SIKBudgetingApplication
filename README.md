# SIK (Saving Is Key) Budgeting Application

## Project Title
**SIK Budgeting App**

## Team Members
- Bradley Bensch (ST10300512)  
- Rubben Shisso (ST10345300)  
- Emma Jae Dunn (ST10301125)

## GitHub Repository
https://github.com/emmajaedunn/SIKBudgetingApplication.git

## YouTube Link
https://youtu.be/J2fFkTsCvVI?si=rVIDh6t5UnH3Qxw0

## Demo Login

You can use the following demo credentials to explore the app:

- **Email:** admin@gmail.com  
- **Password:** AdminUser123!

---

## Overview

SIK (Saving Is Key) is a mobile budgeting app tailored for students and young adults to take control of their financial habits. Built in Kotlin for Android, the app empowers users to:

- Track daily and recurring income/expenses  
- Set and achieve monthly savings goals  
- Visualise spending habits with interactive charts  
- Store data securely via Firebase  

---

## Features

### Core Functionality
- **Recurring Transactions** – Automatically repeat transactions like rent or subscriptions
- **Manual Account Management** – Track balances across multiple accounts
- **Receipt Upload & Preview** – Attach photos of receipts to each transaction
- **Transaction Categories** – Organise income and expenses by customisable categories
- **Budget Goals** – Set min/max limits and track your savings progress
- **Graphs & Reports** – View pie/bar charts of spending by category, budget tracking and achievement progress and level
- **Firebase Integration** – Secure storage, authentication, and data syncing

### Custom Final Features

#### 1. Multi-Currency Support
- Currency selection at registration or via settings
- Support for ZAR, USD, and more
- Currency preference is stored per user in Firebase

#### 2. Notifications & Fax Page (Help & Support)
- Budget alert notifications using Android's `NotificationManager`
- Fax Page includes:
  - App help and FAQs
  - Contact info for support
  - User feedback option

---

## Firebase Integration

### Services Used
- **Authentication** – Firebase Email/Password login
- **Realtime Database** – Secure, structured cloud data storage

### Firebase Setup Steps
1. Created Firebase project  
2. Added app package to Firebase console  
3. Downloaded `google-services.json` to project  
4. Enabled Email/Password auth  
5. Set secure database rules  

---

## UI Overview

- **Login & Register** – Firebase-authenticated user management
- **Home** – Dashboard with add transaction, transaction history, analytics and settings 
- **Stats** – Visual analytics of money spent 
- **Add Transaction** – Manual entry with receipt image
- **Transaction History** – View all income and expenses 
- **Settings**:
- **Budget Goals** – Monthly limit settings
- **Multi-currency** – Select your currency 
- **Manage Accounts** – Custom add/delete account category logic
- **Manage Categories** – Custom add/delete transaction category logic
- **View Achievements** – Users level depending on budgeting progress
- **Notifications** – Enable notification for updates and reminders   
- **Help & Support** – Fax info section
- **Sign out** – Log out of session 

---

## Project Plan and Timeline

**Total Duration:** 68 days 
**Start Date:** 03 March 2025  
**End Date:** 08 June 2025

### Phases
1. **Planning & Research**
2. **UI Design in Figma**
3. **Layout and Screen Creation**
4. **Core Feature Implementation**
5. **Firebase Configuration**
6. **Testing & Debugging**
7. **Deployment & Final Documentation**

---

## GitHub & Version Control

- Git repo initialised with project README  
- Code committed consistently with clear messages  
- Branching used for testing and UI updates  
- GitHub Actions:
  - Automated APK build
  - Lint and unit test jobs triggered on push

---

## Learning Outcomes

- Kotlin and Android app architecture  
- Firebase Authentication & Database integration  
- Version control using Git and GitHub  
- Real-time data sync with Firebase  
- Using MPAndroidChart for financial graphs  
- Custom notification development  
- UI/UX design best practices  
- CI/CD automation with GitHub Actions  
- Testing on emulators and physical Android devices

---

## Feedback Summary from Part 2 

### Highlights
- App runs successfully on emulator and device  
- Login, photo capture, and category features function correctly  
- Financial graphs and filtering are implemented well  
- Code is well-commented and structured

### Areas for Improvement
- Submit a proper demonstration video with voiceover  
- Improve UI consistency (fonts, spacing, colours)  
- Minor logic bugs in goal-setting feature to be reviewed

---

## Conclusion

The SIK (Saving Is Key) Budgeting app aims to provide an intuitive, efficient, and
user-friendly budgeting solution for young adults and students striving to achieve
financial independence. By simplifying the budgeting process through features
like recurring transaction tracking, integrated account management, receipt
uploading, and visual graphs, users can gain better control over their finances.
With proper planning and dedication, SIK Budgeting aims to empower its users
to establish healthy financial habits and reach their savings goals with
confidence.
