<p align="center">
  <img src="https://github.com/user-attachments/assets/95624b4c-a454-4d1a-a07d-79b9aa576923" width="180" alt="LevelUp-Soul">
  <h1 align="center">🔥 LevelUp-Soul</h1>
  <p align="center"><b>Кроссплатформенная система геймификации саморазвития</b></p>
  <p align="center">Инженерное решение для трекинга привычек, построенное на принципах внутренней дисциплины и точности данных</p>
  
  <div align="center">
    <img src="https://img.shields.io/badge/version-1.1.5-blue" alt="Версия">
    <img src="https://img.shields.io/badge/Kotlin-Multiplatform-purple?logo=kotlin" alt="KMP">
    <img src="https://img.shields.io/badge/UI-Compose%20Multiplatform-orange" alt="Compose">
    <img src="https://img.shields.io/badge/status-Production-brightgreen" alt="Статус">
    <img src="https://img.shields.io/badge/license-Apache%202.0-yellow" alt="Лицензия">
  </div>
</p>

---

## 📋 Описание проекта
**LevelUp-Soul** — это программный комплекс, разработанный в рамках конкурса «Высший пилотаж» (НИУ ВШЭ). Приложение объединяет механизмы классических трекеров привычек с ролевыми игровыми моделями (RPG). 

**Ключевое отличие:** отказ от внешних триггеров (уведомлений) в пользу формирования внутренней ответственности и использование высокоточных алгоритмов вычисления прогресса.

---

## 🌟 Интерфейс и возможности

<div align="center">
  <img src="https://github.com/user-attachments/assets/cfc0d9f1-91e0-4de7-ac47-00ab43bced3f" width="23%" alt="Интерфейс">
  <img src="https://github.com/user-attachments/assets/3f7d5ff3-53ed-41c6-b177-bd874373a10b" width="23%" alt="Статистика">
  <img src="https://github.com/user-attachments/assets/a552551c-a9b3-422b-bb5e-2348672ca76a" width="23%" alt="Календарь">
  <img src="https://github.com/user-attachments/assets/c6169bca-47c8-44f8-af34-a8a2e6826cda" width="23%" alt="Диаграммы">
</div>

- **Многоплатформенность:** Нативная работа на Android, Windows, Linux, MacOS и Web (Wasm).
- **Математическая точность:** Использование `BigDecimal` (через библиотеку BigNum) для исключения погрешностей при расчете прогресса.
- **Геймификация:** Интегральный показатель «Уровень Души», завязанный на дисциплине (алгоритм 20-дневных циклов).
- **Аналитика:** Визуализация данных через интерактивные диаграммы и тепловые карты календаря.
- **Privacy First:** Полное локальное хранение данных в формате JSON/XML/LocalStorage без передачи на сторонние сервера.

---

## 🛠 Технологический стек
- **Язык:** Kotlin 2.1.0+
- **Фреймворк:** Compose Multiplatform (UI для всех платформ)
- **Архитектура:** MVVM (Model-View-ViewModel)
- **Хранение данных:** Multiplatform Settings / DataStore
- **Сборка:** Gradle (KMP конфигурация)

---

## 🏗 Инструкция по сборке

Для запуска проекта локально необходимо наличие **JDK 17+** и **Android Studio (Ladybug+)**.

1. Клонируйте репозиторий:
```bash
git clone https://github.com/Forge-of-Ovorldule/LevelUp-Soul.git
```

2. Запуск Desktop-версии (Windows/Linux/MacOS):
```bash
./gradlew :composeApp:run
```


3. Сборка Android-версии:
```bash
./gradlew :composeApp:assembleDebug

```



---

## 🧠 Принципы проектирования (Философия)

> **Почему нет пуш-уведомлений?** > Проект следует концепции осознанного саморазвития. Мы считаем, что внешние уведомления превращают привычку в механическое действие по сигналу. LevelUp-Soul фокусируется на воспитании внутренней тяги к росту.

> **Локальность данных** > Все вычисления и хранение логов происходят на устройстве пользователя. Это гарантирует 100% конфиденциальность и доступность функционала без интернета.

---

## 📥 Загрузка

| Платформа | Источник |
| --- | --- |
| **Android** | [Скачать в RuStore](https://www.rustore.ru/catalog/app/fireforestsoul.levelupsoul) |
| **Desktop / APK** | [GitHub Releases](https://github.com/Forge-of-Ovorldule/LevelUp-Soul/releases) |
| **Web (Demo)** | [Открыть в браузере](https://forge-of-ovorldule.github.io/LevelUp-Soul-site/) |

---

## 🛣 Roadmap

* [x] v1.1.3 — Обновление сохранений и улучшение их стабильности.
* [x] v1.1.4 — Исправление всех накопившихся багов.
* [x] v1.1.5 — Дополнительное мини-обовление статистики привычек.
* [ ] v1.1.6 — Исправление багов и улучшение стабильности.
* [ ] v1.2.0 — Группы привычек.
* [ ] v2.0.0 — Формирование единой экосистемы планирования.

---

## 📜 Лицензия

Распространяется под лицензией **Apache License 2.0**. Подробности в файле [LICENSE](https://www.google.com/search?q=LICENSE).

---

### Используемые материалы
В проекте используется шрифт **[JetBrains Mono](https://www.jetbrains.com/lp/mono/)**, разработанный компанией JetBrains.
Шрифт распространяется под лицензией **[SIL Open Font License 1.1](https://scripts.sil.org/OFL)**.
Copyright © 2020 JetBrains s.r.o.

---

<p align="center">Создано с 🧠 и 🔥 в 2025-2026 гг.</p>
