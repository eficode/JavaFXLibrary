# Contributing

These guidelines instruct how to submit issues and contribute code to the JavaFXLibrary.

## Submitting Issues

Bugs and enhancements are tracked in the issue tracker. If you are unsure if something is a bug or is a feature worth implementing, you can first ask on on [Slack](http://robotframework.slack.com) #JavaFXLibrary channel. These and other similar forums, not the issue tracker, are also places where to ask general questions.

Before submitting a new issue, it is always a good idea to check is the same bug or enhancement already reported. If it is, please add your comments to the existing issue instead of creating a new one.

## Code Contributions

On GitHub pull requests are the main mechanism to contribute code. They
are easy to use both for the contributor and for person accepting
the contribution, and with more complex contributions it is easy also
for others to join the discussion. Preconditions for creating a pull
requests are having a [GitHub account](https://github.com/),
installing [Git](https://git-scm.com>) and forking the
_JavaFXLibrary project_.

GitHub has good articles explaining how to
[set up Git](https://help.github.com/articles/set-up-git/),
[fork a repository](https://help.github.com/articles/fork-a-repo/) and
[use pull requests](https://help.github.com/articles/using-pull-requests)
and we do not go through them in more detail. We do, however,
recommend to create dedicated branches for pull requests instead of creating
them based on the master branch. This is especially important if you plan to
work on multiple pull requests at the same time.

##Tests

When submitting a pull request with a new feature or a fix, you should
always include tests for your changes. These tests prove that your changes
work, help prevent bugs in the future, and help document what your changes
do. Depending an the change, you may need acceptance tests, unit tests
or both.

Make sure to run all of the tests before submitting a pull request to be sure
that your changes do not break anything. 

## Finalizing Pull Requests

Once you have code, documentation and tests ready, it is time to finalize
the pull request.

### AUTHORS.txt

If you have done any non-trivial change and would like to be credited,
add yourself to _AUTHORS.txt_ file.

### Resolving Conflicts

Conflicts can occur if there are new changes to the master that touch the
same code as your changes. In that case you should [sync your fork](https://help.github.com/articles/syncing-a-fork) and [resolve conflicts](https://help.github.com/articles/resolving-a-merge-conflict-from-the-command-line)
to allow for an easy merge.

### Squashing Commits

If the pull request contains multiple commits, it is recommended that you
squash them into a single commit before the pull request is merged.
See [Squashing Github pull requests into a single commit](http://eli.thegreenplace.net/2014/02/19/squashing-github-pull-requests-into-a-single-commit)
article for more details about why and how.

Squashing is especially important if the pull request contains lots of
temporary commits and changes that have been later reverted or redone.
Squashing is not needed if the commit history is clean and individual
commits are meaningful alone.