# 用户模块API

###  用户注册-POST：`/persons/user`

请求：json

``` json
{
  "username": "user1",
  "password": "user123456",
  "ethnic": "汉族",
  "realName": "用户1",
  "phoneNum": "15202884390"(选填)
}
```

响应：json

```json
{
    "code": 200,
    "message": "注册成功",
    "data": null
}
```





### 管理员添加-POST：`/persons/admin`

请求：json

**请求头：Authorization：JWT**

```json
{
  "username": "admin1",
  "password": "admin123456",
  "ethnic": "汉族",
  "realName": "管理员1",
  "phoneNum": "15202884390"(选填)
}
```

响应：json

```json
{
    "code": 200,
    "message": "注册成功",
    "data": null
}
```

 

### 用户登录-POST：`/login/user`

请求：x-www-form-urlendoded

```
username: user1
password: user123456
remember-me: false
```

响应：json

**响应头：** 1.**Authorization：JWT**

2. **当remember-me为true时**：

**Set-Cookie：remember-me=eVlhUDU3Q1g2JTJGRzA0MjNJVFBrNGRRJTNEJTNEOjdNJTJCbGRLTnpZS3hQdSUyRkNiWXZQR2FnJTNEJTNE; Max-Age=1000000; Expires=Thu, 02 May 2024 01:54:39 GMT; Path=/; HttpOnly**

```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "id": 33201286476000169,
        "createdAt": "2024-04-20T19:35:46",
        "updatedAt": "2024-04-20T19:36:49",
        "isDelete": null,
        "username": "gemini",
        "ethnic": "藏族",
        "avatar": "",
        "realName": "leo",
        "gender": 1,
        "role": "user",
        "workAt": "成都信息工程大学",
        "studentId": "2000082001",
        "state": 1,
        "email": "21256116@qq.com",
        "phoneNum": "15202884387",
        "password": "$2a$05$IPhXxnQLCAMNTVww/5i6f.tal8PmhKFqAeluGFikTh2aacls8Y7D2",
        "enabled": true,
        "authorities": [
            "USER"
        ],
        "accountNonExpired": true,
        "credentialsNonExpired": true,
        "accountNonLocked": true
    }
}
```

### 管理员登录-POST：`/login/admin`

请求：x-www-form-urlendoded

```json
username: superadmin
password: admin123456
remember-me: false
```

响应：json

**响应头：** 1.**Authorization：JWT**

2. **当remember-me为true时**：

**Set-Cookie：remember-me=eVlhUDU3Q1g2JTJGRzA0MjNJVFBrNGRRJTNEJTNEOjdNJTJCbGRLTnpZS3hQdSUyRkNiWXZQR2FnJTNEJTNE; Max-Age=1000000; Expires=Thu, 02 May 2024 01:54:39 GMT; Path=/; HttpOnly**

```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "id": 1,
        "createdAt": "2024-04-20T20:13:35",
        "updatedAt": "2024-04-20T20:13:36",
        "isDelete": null,
        "username": "superadmin",
        "ethnic": "汉族",
        "avatar": "https://online-services-for-ethnic.oss-cn-beijing.aliyuncs.com/default-avatar.jpg",
        "realName": "超级管理员",
        "gender": 0,
        "role": "admin",
        "workAt": null,
        "studentId": null,
        "state": 1,
        "email": null,
        "phoneNum": null,
        "password": "$2a$05$Fw3qIdd9Gq6YmxEBq9DVT.DvwPNV.vMjdIGOOSvPiLi35oJ4XnETy",
        "enabled": true,
        "authorities": [
            "ADMIN"
        ],
        "accountNonExpired": true,
        "credentialsNonExpired": true,
        "accountNonLocked": true
    }
}
```

### 修改密码-PUT：`/persons/password`

请求：json

**请求头：Authorization：JWT**

```json
{
    "oldPassword": "user12345",
    "newPassword": "user123456"
}
```

响应：json

```json
{
    "code": 200,
    "message": "修改密码成功",
    "data": null
}
```

### 获取当前用户信息-GET：`/persons/info` 

请求：无

**请求头：Authorization：JWT**

响应：json

``` json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": "33199822880000196",
        "createdAt": "2024-04-20 19:11:22",
        "updatedAt": "2024-04-20 19:18:41",
        "isDelete": null,
        "username": "user1",
        "ethnic": "汉族",
        "avatar": "https://online-services-for-ethnic.oss-cn-beijing.aliyuncs.com/default-avatar.jpg",
        "realName": "用户1",
        "gender": 0,
        "role": "user",
        "workAt": null,
        "studentId": null,
        "state": 1,
        "email": null,
        "phoneNum": "152******90",
        "password": "$2a$05$ENaAsapEEYfBVElEkfl93.BlsSa8jHeOIRSctNyuX0VyszLtIhpN.",
        "authorities": [
            "USER"
        ],
        "credentialsNonExpired": true,
        "accountNonExpired": true,
        "accountNonLocked": true,
        "enabled": true
    }
}
```

### 根据id获取用户-GET：`persons/info/{id}`

请求：无

**请求头：Authorization：JWT**

响应：json

```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": "33201286476000169",
        "createdAt": "2024-04-20 19:35:46",
        "updatedAt": "2024-04-20 19:51:18",
        "isDelete": 0,
        "username": "gemini",
        "ethnic": "藏族",
        "avatar": "https://online-services-for-ethnic.oss-cn-beijing.aliyuncs.com/default-avatar.jpg",
        "realName": "leo",
        "gender": 1,
        "role": "user",
        "workAt": "成都信息工程大学",
        "studentId": "2000082001",
        "state": 1,
        "email": "21256116@qq.com",
        "phoneNum": "15202884387",
        "password": "$2a$05$IPhXxnQLCAMNTVww/5i6f.tal8PmhKFqAeluGFikTh2aacls8Y7D2",
        "enabled": true,
        "authorities": [
            "USER"
        ],
        "accountNonExpired": true,
        "credentialsNonExpired": true,
        "accountNonLocked": true
    }
}
```



### 更新当前用户-PUT：`persons/info/{id}`

请求：json

**请求头：Authorization：JWT**

```json
{
  "username": "gemini",
  "phoneNum": "15202884387",
  "ethnic": "藏族",
  "avatar": "url",
  "realName": "leo",
  "gender": 1,
  "workAt": "成都信息工程大学",
  "studentId": "2000082001",
  "email": "21256116@qq.com"
}
```

响应：json

```json
{
    "code": 200,
    "message": "更新成功",
    "data": null
}
```

### 退出登录-GET：`/logout`

请求：无

响应：json

```json
{
    "code": 200,
    "message": "退出成功",
    "data": null
}
```

### 文件上传-POST：`/image/upload`

请求：form-data

**请求头：Authorization：JWT**

```
file: picture.png(文件)
```

响应：json

```json
{
    "code": 200,
    "message": "Success",
    "data": "https://online-services-for-ethnic.oss-cn-beijing.aliyuncs.com/6642d4f9-b3a0-4ac0-9de5-13c6fe197ff2.png" 
    //返回该文件的访问URL
}
```

###  备注：

**1.修改头像时，先请求文件上传接口，将返回的URL传给更新用户信息`persons/info/{id}`的请求参数avatar**

**2.登录选择“记住我”时，后端会创建了一个带有令牌的Cookie：remember-me，并将这个Cookie发送给前端。前端浏览器接收到这个Cookie后，会在每次请求时自动将其发送给后端服务器。前端在接收到这个Cookie后，通常不需要进行任何操作。Cookie会在每次请求中自动包含在HTTP头中，后端会负责解析这个Cookie，并验证其中的令牌。如果令牌有效，用户将自动登录，无需再次输入用户名和密码。**

