# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Added GitHub Actions workflow for building release APK.
- Updated dependencies to latest stable versions (Kotlin 2.3.0, AGP 8.8.0, etc.).
- Migrated map implementation from OSMDroid to MapLibre Native for better performance and vector tiles support.

### Changed
- Bumped `compileSdk` and `targetSdk` to 35.
- Update `hiddenapibypass` to 6.1.
- Refactored UI to use MapLibre Compose components.

### Removed
- Removed OSMDroid dependency and its initialization code.
- Removed "Community" and "App Info" sections from the navigation drawer.
