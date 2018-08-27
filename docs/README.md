# Jellyfish Docs
This is the GitHub Pages site for the Jellyfish documentation and books.  The site is hosted at
[https://pages.github.ms.northgrum.com/CEACIDE/jellyfish](https://pages.github.ms.northgrum.com/CEACIDE/jellyfish).

# Building and Serving the Site with Jekyll
It's possible to host this site with a local instance of Jekyll.  This is useful for offline deployments and testing.
Jekyll supports Windows installations but the Linux setup is easier.

## Installing Jekyll
You first need to install Ruby.  If you are using a bare Linux distribution like CentOS you will need to install
development tools first
```
sudo yum groupinstall -y 'Development Tools'
```

Next, install RVM and use RVM to install v2.5.0 of Ruby (2.5.0 or above is required by Jekyll):
```
curl -sSL https://rvm.io/mpapis.asc | gpg2 --import -
curl -sSL get.rvm.io | bash -s stable
source /etc/profile.d/rvm.sh
rvm install 2.5.1
```

Install and run Bundler to complete the setup.  Do this from the directory that contains the `Gemfile`.
```
source /etc/profile.d/rvm.sh
rvm use 2.5.1
gem install bundler
bundle install
```

## Building and Deploying the Site
Once the Jekyll gem is installed, clone this repository and run the following:
```
source /etc/profile.d/rvm.sh
rvm use 2.5.1
bundle exec jekyll serve
```

Jekyll will display where the site is being hosted.  If the last command fails (due to the GitHub themes plugin not
being found) try running
```
bundle update
bundle install
```
Then try `bundle exec jekyll serve` again.

## Accessing the Site From a Network
By default, Jekyll will only serve the site on the local machine.  If you want to be able to access the site from other
resources on the network use
```
bundle exec jekyll serve --host 0.0.0.0
```
