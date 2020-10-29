# Setting up upload webserver with apache
* Move the content of package "resources/upload-webserver" to /var/www/html
* Set up an apache2 webserver
* Install mysql service with:
```
sudo apt install mysql-server
```

* Install php with:
```
sudo apt install php
```

* Restart apache and mysql service with 
```
sudo service apache2 restart
```
```
sudo service mysql restart
```
* Connect to localhost/post_upload/post_upload.html
* Browse and select a large file to upload
* Select upload
