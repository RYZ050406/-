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
/give @p flyable:temporary_flight_slime_ball
```

`flyable:flight_slime_ball` 右键空气或方块切换无限飞行，没有冷却。

`flyable:temporary_flight_slime_ball` 右键开启最多 10 秒的飞行；飞行结束或再次右键提前结束后，进入 30 秒冷却。这个冷却只影响限时飞行粘液球，不影响普通飞行粘液球。

开启时仍保持当前游戏模式，不会切成创造模式；关闭时会按当前游戏模式恢复飞行权限，避免创造模式飞行被误关。
