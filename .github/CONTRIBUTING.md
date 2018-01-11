# Contributing to OpenRC

The following is a set of guidelines for contributing to OpenRC. The OpenFTC community welcomes new features and bug
reports for all of our projects. Our goal is to provide a place where any member of the community with something valuable
to contribute can do so.

Note: This is based on the official FIRST-provided FTC SDK (ftc_app), but it is NOT endorsed or run by FIRST. Most, if
not all, changes made here will likely never be merged into the official SDK, but OpenRC will be kept up to date with
official features.

## How can I contribute?

### Report bugs

This section guides you through filing a bug report.  The better the report, the more likely it is to be root caused and
fixed. Even if you think that the bug likely also exists in the official SDK, don't report it there unless you take the
time to reproduce it with the official version. FIRST doesn't want to support unofficial code.

#### Before submitting a bug report

- Perform a search of current [OpenRC issues](https://github.com/OpenFTC/OpenRC/issues) to see if the problem has
  already been reported.  If so, add a comment to the existing issue instead of creating a new one.

- Perform a search of current [official ftc_app issues](https://github.com/ftctechnh/ftc_app/issues) to see if the
  problem has already been reported.  If so, add a comment to the existing issue instead of creating a new one.

- Check the [forums](http://ftcforum.usfirst.org/forum.php) to see if someone else has run into the problem and whether
  there is an official solution that has either 1) already been merged into the official SDK, or 2) is being
  investigated by the FIRST Tech team.


#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as GitHub issues.
[Create an issue on the OpenRC repository](https://github.com/OpenFTC/OpenRC/issues/new)
and provide the following information.
Explain the problem and include additional details to help maintainers reproduce the problem:

- Use a clear and descriptive title for the issue to identify the problem.

- Describe the exact steps which reproduce the problem in as many details as possible.

- Provide specific examples to demonstrate the steps.

- Describe the behavior you observed after following the steps and point out what exactly is the problem with that
  behavior. Explain which behavior you expected to see instead and why. If applicable, include screenshots which show you
  following the described steps and clearly demonstrate the problem.

- If you're reporting that the RobotController crashed, include the logfile with a stack trace of the crash.
  [Example of good bug report with stack trace](https://github.com/ftctechnh/ftc_app/issues/224)

- If the problem wasn't triggered by a specific action, describe what you were doing before the problem happened and
  share more information using the guidelines below.


### Pull requests

1. Before you start coding a new feature, create an issue to ask if it's something that the maintainers would want to
   have as a part of the OpenRC app. If there is an existing issue tracking a similar feature request, comment on it to
   say that you're going to work on it, so that we don't have duplicated efforts.

2. Create a new branch based off the current OpenRC `develop` branch. That way, anything you can change things on
   your master branch without those changes getting added to the pull request. It also allows us to safely do squash merges.

3. Any code you change should be marked with `// Modified for Turbo`, unless it is brand new code, is in an
   OpenRC-specific file, is an import statement, or is something that can safely be overwritten by changes to the
   official SDK.

4. Make sure your pull request has the `develop` branch as its base.

5. If your pull request takes care of a filed issue, put "closes #23" (replace with the number of the issue to be closed)
   in the pull request description. If the issue is a bug, you can replace "closes" with "fixes".

More guidelines to come in the future.

### Suggesting Enhancements

We will at least consider any enhancement that does not force teams to change their workflow. Changes made in OpenRC
should allow teams to move from the official SDK to OpenRC with no code changes required.

To request a new feature, you can open an issue on this repository. You may want to discuss your ideas on the OpenFTC
Discord server or the FTC forums, and they may even be added to the official SDK. If there's a large enough call for the
feature, it's very likely to be added to the list for a future release of OpenRC.

Don't be afraid to build out a feature yourself! To merge it into OpenRC, follow the pull request guidelines above.
We'll help you work through the process of getting it ready for prime time.
