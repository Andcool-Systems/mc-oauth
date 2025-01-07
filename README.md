# Minecraft OAuth Provider

## 🚀 Description
A simple way to add Minecraft authentication to your website or project.

Many developers who have tried to implement user authentication through Minecraft's official system using Microsoft have encountered various difficulties. One of the main issues is the requirement to create and get approval for an authentication application directly from Microsoft, which can be a challenging task.

## 💡 Why mc-oauth?
Our service provides an authentication system that does not require complex actions from either users or developers.

### 🔑 Features
- 📋 Retrieve nickname and UUID via REST API
- 🛡️ Zero Trust (Impossible for users to spoof Minecraft account data)
- ⚡ Easy to use and implement
- 🔒 The server handles only the authentication flow and nothing more

## 💻 For Users
1. Open Minecraft and connect to the server that uses this service.
2. After connecting, the server will kick you, returning your unique code in a message.
3. Enter the received code on the website that using our service.
4. Enjoy the result ❤.

## 🛠️ For Developers
1. Add a form on your website or project for entering a 6-digit code.
2. After the user inputs the code provided by the server upon login, make a request to the API endpoint described below.

### 📡 API Endpoint
```
GET /code/<6-digit code>
```

### Example of a successful server response:
```json
{
  "statusCode": 200,
  "nickname": "AndcoolSystems",
  "UUID": "1420c63cb1114453993fb3479ba1d4c6"
}
```

> [!NOTE]
> The code can only be used once and is valid for up to 5 minutes after it is issued to the client. After that, it is automatically deleted.
