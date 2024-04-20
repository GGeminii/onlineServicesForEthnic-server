# IntelliParkServer

> 智能园区服务端
>
> 本项目后端技术主要基于
>
> SpringBoot-3.X
>
> (SpringSecurity+JWT)
>
> Mybatis-Flex
>
> Redis
>
> Quartz
>
> Swagger
>
> OSS图床

## Branches

+ `main`: 主分支，生产版本
+ `dev`: 开发分支，开发版本

## Manual

### [API](https://app.apifox.com/invite?token=E-BiwuxhGGHSsQOCOxfA1)

### [Git远程仓库](https://g-erad5283.coding.net/p/IntelliPark/d/IntelliParkServer/git)

### 项目结构

```txt
└─onlineServicesForEthnic-server
    │  .gitignore
    │  build.gradle.kts
    │  gradlew
    │  gradlew.bat
    │  README.md
    │  settings.gradle.kts
    ├─gradle
    │  └─wrapper
    │          gradle-wrapper.jar
    │          gradle-wrapper.properties
    │
    ├─onlineServicesForEthnic-api
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  │  └─onlineServicesForEthnic
    │      │  │          onlineServicesForEthnicApplication.java
    │      │  │
    │      │  └─resources
    │      │      │  application-dev.yml
    │      │      │  application.yml
    │      │      │
    │      │      ├─static
    │      │      └─templates
    │      └─test
    │          ├─java
    │          │  └─onlineServicesForEthnic
    │          │          onlineServicesForEthnicApplicationTests.java
    │          │
    │          └─resources
    ├─onlineServicesForEthnic-common
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  └─resources
    │      └─test
    │          ├─java
    │          └─resources
    ├─onlineServicesForEthnic-dao
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  └─resources
    │      └─test
    │          ├─java
    │          └─resources
    ├─onlineServicesForEthnic-pojo
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  └─resources
    │      └─test
    │          ├─java
    │          └─resources
    └─onlineServicesForEthnic-service
        └─src
            ├─main
            │  ├─java
            │  └─resources
            └─test
                ├─java
                └─resources
...
...
```

### 调试须知

启动类位于``onlineServicesForEthnic-api``中的``onlineServicesForEthnicApplication.java``
在向启动配置中配置数据库源和OSS配置  
`application-dev.yml`  
以启用`dev`开发配置文件

### 合并须知

1、开发某模块前请**提前商量告知**，开发按照规范，以免开发冲突

2、先将git仓库coding中的onlineServicesForEthnic-server项目**派生**到自己仓库

3、并**时常更新**，以免导致项目不同步导致合并审核困难

4、推送到仓库时**尽量描述已完成功能**以便审核

5、**请勿**将application-dev.yml中的敏感配置推送上来，或者选择不推送application-dev.yml文件

### 项目规范

#### 命名

+ 类型名：自身类型加上从属种类`BookService`,`BookController`,`BookMapper`,`BookConfig`等
+ 类型名大驼峰`LogPrinter`
+ 变量名小驼峰`entityBooklist`
+ 数据库各项名统一使用小写加下划线`booklist_id`
+ API_VERSION：
    + 如果架构变动，则主版本号加1
    + 如果增删接口，则次级版本号加1
    + 如果对接口有修改，则次次级版本号加1

#### 语法

1、成员变量调用时一律加上**this**
`this.id`

2、所有的字段访问类型修饰符都要写明  
`private int id`、`public static final int id`

3、**不允许使用非法字段注入，使用构造函数注入**

 lombok注解``@RequiredArgsConstructor``

```java
/**
 * <p>构造函数注入</p>
 */
public class ServiceUser {

    private final RepositoryUser repositoryUser;

    @Autowired
    public ServiceUser(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
    }
}

/**
 * <p>或者构造函数注入（lombok注解形式）</p>
 */
@RequiredArgsConstructor
public class ServiceUser {

    private final RepositoryUser repositoryUser;
}
```

```kotlin
/**
 * # 构造函数注入
 */
class ServiceUser @AutoWired constructor(
        private val repositoryUser: RepositoryUser
) {
    // 类体...
}

```

#### 注释

每个类的用途写明，格式如下所示

> JavaDoc  
> KDoc

##### 类注释

```java
/**
 * <p>这是一个用于演示的类注释示例。</p>
 *
 * <p>该类提供了一些有用的功能，如计算平均值、查找最大值等。</p>
 *
 */
public class ExampleClass {
    // 类体...
}
```

```kotlin
/**
 * # 这是一个用于演示的类注释示例。
 *
 * 该类提供了一些有用的功能，如计算平均值、查找最大值等。
 *
 */
```

##### 方法注释

```java
class example {
    /**
     * <p>计算给定整数数组的平均值，并指定是否排除数组中的最大值和最小值。</p>
     *
     * <p>如果数组为空，则返回0。</p>
     *
     * @param numbers 要计算平均值的整数数组。
     * @param excludeMax 是否在计算平均值时排除数组中的最大值。
     * @param excludeMin 是否在计算平均值时排除数组中的最小值。
     * @return 整数数组的平均值，或者在数组为空时返回0。
     * @throws IllegalArgumentException 如果输入的数组为null。
     */
    public double calculateAverage(int[] numbers, boolean excludeMax, boolean excludeMin) throws IllegalArgumentException {
        // 方法体...
    }
}
```

```kotlin
class Example {
    /**
     * # 计算给定整数数组的平均值，并指定是否排除数组中的最大值和最小值。
     *
     * 如果数组为空，则返回0。
     *
     * @param numbers 要计算平均值的整数数组。
     * @param excludeMax 是否在计算平均值时排除数组中的最大值。
     * @param excludeMin 是否在计算平均值时排除数组中的最小值。
     * @return 整数数组的平均值，或者在数组为空时返回0。
     * @throws IllegalArgumentException 如果输入的数组为null。
     */
    fun calculateAverage(numbers: IntArray, excludeMax: Boolean, excludeMin: Boolean): Double {
        // 方法体...
    }
}
```

### knife4j规范

**swagger注释写明api用于测试**

#### [swagger2的注解改变说明](https://blog.csdn.net/Mrqiang9001/article/details/132305015)

```
@Api → @Tag
@ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
@ApiImplicitParam → @Parameter
@ApiImplicitParams → @Parameters
@ApiModel → @Schema
@ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
@ApiModelProperty → @Schema
@ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
@ApiParam → @Parameter
@ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")
```

#### 主要使用注解

##### api注解

``@Tag(name = "模块名称")``与``@Operation(summary = "测试Accept",description = "api描述")``

```
@Tag(name = "模块名称")
@RequestMapping(value = "/accept",produces = "application/vnd.dim.s.v1+json")
@RestController
public class AcceptController {

    @Operation(summary = "测试Accept",description = "api描述")
    @GetMapping("/get")
    public ResponseEntity<User> get(User user){
        return ResponseEntity.ok(user);
    }
}
```

##### 模型注解

``@Schema``

```
@Builder
public class User {

    @Schema(title = "userId",description = "主键id",defaultValue = "1")
    private String id;

    @Schema(description = "名称",defaultValue = "张飞",nullable = true,requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    
}
```

WIP...