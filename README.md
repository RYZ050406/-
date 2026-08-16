# flyable Fabric 模组

Minecraft `1.21.11` 的 Fabric 小模组，新增一个可以在生存模式下右键切换高速飞行的粘液球。

## 使用

需要先安装：

- JDK 21
- Gradle 9.2+，或用 IntelliJ IDEA 打开项目后让它导入 Gradle 项目

构建：

```powershell
gradle build
```

如果你想生成 Gradle Wrapper，可以在安装 Gradle 后运行：

```powershell
gradle wrapper --gradle-version 9.2.0
.\gradlew build
```

构建完成后，把 `build/libs/flyable-1.0.0.jar` 放进客户端或服务端的 `mods` 文件夹。

这个构建会把 Fabric API 作为 jar-in-jar 放进模组包里，所以正常情况下只需要安装 Fabric Loader，不需要再单独放 Fabric API。

在游戏里获取物品：

```mcfunction
/give @p flyable:flight_slime_ball
```

右键空气或方块切换飞行。开启时仍保持当前游戏模式，不会切成创造模式；再次右键会恢复开启前的飞行权限和飞行速度。
