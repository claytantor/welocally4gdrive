package com.welocally.gdrive.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * {
   "kind": "drive#file",
   "id": "0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE",
   "etag": "\"UlsjfRuhDohcPE5NueFaUCqyYTY/MTM0NTUxNDQ5MzM0MA\"",
   "selfLink": "https://www.googleapis.com/drive/v2/files/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE",
   "alternateLink": "https://docs.google.com/a/welocally.com/spreadsheet/ccc?key=0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE",
   "embedLink": "https://docs.google.com/a/welocally.com/spreadsheet/ccc?key=0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE&output=html&chrome=false&widget=true",
   "thumbnailLink": "https://docs.google.com/feeds/vt?gd=true&id=0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE&v=21&s=AMedNnoAAAAAUD8aPxu7m8xMyPzPAh59QBVzMxI1SaQj&sz=s220",
   "title": "nyc_tailors",
   "mimeType": "application/vnd.google-apps.spreadsheet",
   "labels": {
    "starred": false,
    "hidden": false,
    "trashed": false,
    "restricted": false,
    "viewed": true
   },
   "createdDate": "2012-05-07T17:16:14.776Z",
   "modifiedDate": "2012-08-21T02:01:33.340Z",
   "modifiedByMeDate": "2012-08-21T02:01:33.340Z",
   "lastViewedByMeDate": "2012-08-21T01:58:27.933Z",
   "parents": [
    {
     "kind": "drive#parentReference",
     "id": "0APb_MOw4lfVnUk9PVA",
     "selfLink": "https://www.googleapis.com/drive/v2/files/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE/parents/0APb_MOw4lfVnUk9PVA",
     "parentLink": "https://www.googleapis.com/drive/v2/files/0APb_MOw4lfVnUk9PVA",
     "isRoot": true
    }
   ],
   "exportLinks": {
    "application/pdf": "https://docs.google.com/feeds/download/spreadsheets/Export?key=0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE&exportFormat=pdf",
    "application/x-vnd.oasis.opendocument.spreadsheet": "https://docs.google.com/feeds/download/spreadsheets/Export?key=0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE&exportFormat=ods",
    "application/vnd.ms-excel": "https://docs.google.com/feeds/download/spreadsheets/Export?key=0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE&exportFormat=xls"
   },
   "userPermission": {
    "kind": "drive#permission",
    "etag": "\"UlsjfRuhDohcPE5NueFaUCqyYTY/eMYrV1p8O23w3Xz_audsRMDGgis\"",
    "id": "me",
    "selfLink": "https://www.googleapis.com/drive/v2/files/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE/permissions/me",
    "role": "owner",
    "type": "user"
   },
   "quotaBytesUsed": "0",
   "ownerNames": [
    "Clay Graham"
   ],
   "lastModifyingUserName": "Clay Graham",
   "editable": true,
   "writersCanShare": true
  }
 * @author claygraham
 *
 */
@JsonIgnoreProperties({
    "kind","etag","selfLink","alternateLink","embedLink","thumbnailLink",
    "labels","createdDate","modifiedDate","modifiedByMeDate","lastViewedByMeDate",
    "parents","exportLinks","userPermission","quotaBytesUsed","ownerNames","lastModifyingUserName",
    "editable","writersCanShare","fileExtension","md5Checksum","downloadUrl","webContentLink",
    "fileSize","sharedWithMeDate","originalFilename","imageMediaMetadata","explicitlyTrashed"
    })
public class DocumentItem {
    private String id;
    private String title;
    
    //application/vnd.google-apps.spreadsheet
    private String mimeType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    
}
