# flyable Fabric 模组

Minecraft `1.21.11` 的 Fabric 小模组，新增一个可以在生存模式下右键切换高速飞行的粘液球。

## 使用

需要：

- JDK 21
- 游戏客户端或服务端需要安装 Fabric API `0.141.6+1.21.11` 或兼容版本

从仓库根目录构建：

```powershell
.\gradlew.bat buildFlyable
```

或进入本模组目录构建：

```powershell
cd mods\flyable
..\..\gradlew.bat build
```

构建完成后，把 `mods/flyable/build/libs/flyable-1.0.0.jar` 放进客户端或服务端的 `mods` 文件夹。

这个构建不会内置 Fabric API，需要在 `mods` 文件夹里单独放 Fabric API。

在游戏里获取物品：

```mcfunction
/give @p flyable:flight_slime_ball
/give @p flyable:temporary_flight_slime_ball
```

`flyable:flight_slime_ball` 右键空气或方块切换无限飞行，没有冷却。

`flyable:temporary_flight_slime_ball` 右键开启最多 10 秒的飞行；飞行结束或再次右键提前结束后，进入 30 秒冷却。这个冷却只影响限时飞行粘液球，不影响普通飞行粘液球。

开启时仍保持当前游戏模式，不会切成创造模式；关闭时会按当前游戏模式恢复飞行权限，避免创造模式飞行被误关。
