# Release guide

The releases are done via a [release](../.github/workflows/release.yml) GitHub action.

Steps:

1. Update the version in `version.txt` and commit with message `[release] 0.1.0`
2. Tag the commit in git in the `v0.1.0` format
3. Push the tag to GitHub

After release:

- Bump version in `version.txt` to the next
- Update the readme with the new version
