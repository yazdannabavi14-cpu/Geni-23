# Geni Assistant (جنی)

نسخه تست Native Android برای دستیار صوتی فارسی نزدیک به Siri.

## قابلیت‌های فعال در نسخه اول
- اجرای سرویس پس‌زمینه با اعلان کوچک و Silent.
- گوش دادن به کلیدواژه «جنی» با SpeechRecognizer اندروید.
- پاسخ صوتی فارسی با TextToSpeech گوشی.
- تماس با مخاطب با فرمان: «جنی به علی زنگ بزن».
- روشن/خاموش کردن بلندگو با فرمان صوتی.
- تنظیم سیم‌کارت پیش‌فرض تماس و پیامک در داخل برنامه.
- افزودن مخاطب دستی برای یادگیری اسم‌ها.
- پشتیبانی پایه از مخاطبین گوشی.

## محدودیت‌های واقعی اندروید
- آیکون/نشانگر حریم خصوصی میکروفون که خود اندروید نشان می‌دهد قابل حذف یا مخفی‌سازی نیست.
- برای کارکرد پایدار در صفحه خاموش، باید Battery Optimization برای جنی خاموش شود.
- روی بعضی برندها مثل Xiaomi / Huawei / Samsung باید Auto-start یا Unrestricted Battery هم فعال شود.
- تشخیص گفتار آفلاین/آنلاین وابسته به سرویس SpeechRecognizer گوشی است.

## ساخت APK در GitHub
1. این پوشه را در یک ریپازیتوری GitHub آپلود کن.
2. از تب Actions، workflow با نام **Build Geni APK** را اجرا کن.
3. بعد از اتمام، از قسمت Artifacts فایل `geni-assistant-debug-apk` را دانلود کن.
4. داخل آن `app-debug.apk` آماده نصب است.

## ساخت لوکال
اگر Android Studio داری:
- پروژه را باز کن.
- Sync Gradle را بزن.
- Build > Build APKs را اجرا کن.



# GENI Enhanced Version

This version adds:
- Wake word structure (Jeni)
- Intent routing system
- Improved voice service structure
- GitHub Actions APK build ready
