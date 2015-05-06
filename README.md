# Git Storage Provider

Since KitKat (4.4), the Android OS exposes a framework called *Storage Providers*, to unify the way document can be seen, edited or created across applications. 

## Features

This application provides : 

  - a Storage Provider, allowing user to read, edit or create documents through any Android app from and into local git repositories;
  - perform simple git operations locally (`pull`, `commit`, `push`, `tag`, `merge`).

## TODO 

  - Create a basic interface to clone repo locally
  - Link the local repo to the storage provider
  - Add some security features : 
    - GPG crypto (edit files clear, stored crypted on the repo)
    - Import SSH config 
  - Repo status notifications : 
    - You've got unstaged changes 
    - You've got uncommited changes
    - Local repo is [X commits ahead] [Y commits behind] origin
  - Brand name, Logo, Design

 
