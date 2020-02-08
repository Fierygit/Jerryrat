# maven

以下的命令会按顺序执行



```
mvn clean
```

把编译好的类清除掉， 因为每个开发环境不同

```
mvn compile
```

编译主要的类

```
mvn test
```

编译测试的类

```
mvn package
```

把工程打包成jar 包

编译测试类

```
mvn install
```

执行上面的所有命令， 并且放入本地仓库中



maven工程可以直接使用：

```
tomcat:run 
```

直接使用tomcat 运行项目





 

