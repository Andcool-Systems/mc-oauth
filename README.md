# Simple Minecraft OAuth Service

This service allows you to implement user login on your website without needing to log in to a Microsoft account.  
Minecraft server IP: **mc-oauth.andcool.ru** (1.12 — 1.20.*)

## How It Works
The plugin is installed on a Bukkit server (you can also add ViaVersion for a wider range of versions).  
After installation, all connections will be disconnected with a message containing a 6-digit code.  
On the server side, the plugin sets up an API on the port specified in the config, where all the requests described below should be sent.

## Retrieving Data
`GET /code/<6-digit code>`  
After the user receives the code, you should send a request to the API endpoint, which will return data about the Minecraft account.  
The code is valid only once and for 5 minutes (by default) after it is received. After this time, the code is deleted.

**Example of a Successful Response:**
```json
{
  "nickname": "AndcoolSystems",
  "UUID": "1420c63c-b111-4453-993f-b3479ba1d4c6",
  "status": "success"
}
```