# Publitio ANDROID SDK

Android SDK for Publitio API. This SDK comes with sample app.

Sample app will return JSON response for functions like

File Create > Upload

```php
{"success":true,"code":201,"message":"File uploaded","id":"i0haVus5","public_id":null,"title":"IMG_20180924_125120","description":"","tags":"","type":"image","extension":"jpg","size":200602,"width":960,"height":1280,"privacy":"public","option_download":"disabled","option_ad":"enabled","option_transform":"disabled","wm_id":null,"url_preview":"https://media.publit.io/file/i0haVus5.jpg","versions":0,"hits":0,"created_at":"2018-10-01 11:43:44","updated_at":"2018-10-01 11:43:44"}d_at":"2018-10-01 11:38:21","updated_at":"2018-10-01 11:38:21"}

Or visit link for preaty version
https://jsonblob.com/4f5f2fdb-c56f-11e8-a5bc-f94935ae111a
```

File List files
```php
{"success":true,"code":200,"limit":100,"offset":0,"files_total":2,"files_count":2,"files":[{"id":"6yMN6jw0","public_id":"PublicIdVIdeo","title":"Instagram Ad 600x600 Jumper GamePlay 60s","description":"My Description","tags":"tags, separated, with, comma","type":"video","extension":"mp4","size":17099068,"width":600,"height":600,"duration":59.630000000000003,"privacy":"public","option_download":"enabled","option_ad":"enabled","option_transform":"enabled","wm_id":null,"url_preview":"https://media.publit.io/file/PublicIdVIdeo.mp4","url_download":"https://media.publit.io/download/PublicIdVIdeo.mp4?at=eyJpdiI6IllkbTBaYnUxK1FcL2RrNVlJYWRHS1JRPT0iLCJ2YWx1ZSI6Ill3b3RiaVpjRFhuaFhpelZ6NEU1RUVwMlhVc3Urb1duZXIwek9aeUhOUkk9IiwibWFjIjoiOWRkYWI5YmQ0OWI4MmRhYTA3OWQyMjIzNjNmZDQzNWZiZGQ5YzIxY2VkM2Q2MmZlMmE5ZjNlNDI5YWM2NGQ0NiJ9","versions":5,"hits":28,"created_at":"2018-09-21 07:56:40","updated_at":"2018-10-01 11:04:48"},{"id":"akb7hD8k","public_id":null,"title":"publitoo logo 600x600x","description":"","tags":"","type":"image","extension":"png","size":15028,"width":600,"height":600,"privacy":"public","option_download":"enabled","option_ad":"enabled","option_transform":"enabled","wm_id":null,"url_preview":"https://media.publit.io/file/akb7hD8k.png","url_download":"https://media.publit.io/download/akb7hD8k.png?at=eyJpdiI6IklkckM1SUpudTZpd1dManRUd1A1cFE9PSIsInZhbHVlIjoiXC9hT3hUTWlzVzB1UTcxRXdOZ0tHVjhmbHg4REtnN0htcnpjWG93bGRvTEk9IiwibWFjIjoiYWY0ZGFiNmVjYmMxNzAxYjBkYzY3NWMyNjQzZTRiYzU2NWI1ZGQwYThmN2Y3YmJmMjgxN2RmODQ0NjkyNmFkNSJ9","versions":0,"hits":0,"created_at":"2018-09-21 08:12:07","updated_at":"2018-09-21 08:12:07"}]}

Or visit link for preaty version
https://jsonblob.com/ff837690-c56e-11e8-a5bc-f5c2a9af53a5
```

Show file response
```php
{"success":true,"code":200,"id":"i0haVus5","public_id":null,"title":"IMG_20180924_125120","description":"","tags":"","type":"image","extension":"jpg","size":200602,"width":960,"height":1280,"privacy":"public","option_download":"disabled","option_ad":"enabled","option_transform":"disabled","wm_id":null,"url_preview":"https://media.publit.io/file/i0haVus5.jpg","versions":0,"hits":0,"created_at":"2018-10-01 11:43:44","updated_at":"2018-10-01 11:43:44"}

Or visit link for preaty version
https://jsonblob.com/ebbd3f4b-c56f-11e8-a5bc-7f6602c2dfec
```

Update file
```php
{"success":true,"code":200,"message":"The file with with id 'i0haVus5' has been updated"}
```


## Installation
Steps to integrate SDK into App:

Android :
https://bintray.com/publitio/publitio-android-sdk/publitio_android_sdk#
Steps to Integrate SDK into App:


Include this into your project gradel

```java
compile "com.publit.io:publit-io:0.0.2"
```

Enter API Secret and API key into manifest as follow.
Make changes in your AndroidManifest

```php
<meta-data
android:name="publitio_api_key"
android:value="YOUR_API_KEY" />


<meta-data
android:name="publitio_api_secret"
android:value="YOUR_API_SECRET" />
```

Initialize Publitio SDK
```java
 //Publitio class object to call Publit.io API's.
 Publitio mPublitio;


 //Initialize Publitio object.
 mPublitio = new Publitio(this);
```   


In Demo app you will find FilesActivity class with specific calls that are in test app.

Upload of files is done in simple call function

```java
Uri uri = data.getData();

Map<String, String> create = new HashMap<>();
//Implement options for files if default ignore all Params
//create.put(CreateFileParams.PRIVACY, FilesPrivacyParams.PUBLIC);
//create.put(CreateFileParams.OPTION_DOWNLOAD, FilesDownloadParams.DISABLE);
//create.put(CreateFileParams.OPTION_TRANSFORM, FilesTransformationParams.DISABLE);
//create.put(CreateFileParams.OPTION_AD, FilesADParams.ENABLE);
//create.put(FilesContants.CreateFile.TITLE, "PICK_IMAGE");

//Calling Publit.io upload file api.
    mPublitio.files().uploadFile(uri, create, new PublitioCallback<JsonObject>() {
        @Override
        public void success(JsonObject result) {
        Log.d("Publitio", "file uploaded: " + result.toString());

        }

        @Override
        public void failure(String message) {
         
        Log.d("Publitio", "file upload error: " + message.toString());

         }
        });

```





