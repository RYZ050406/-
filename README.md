# 阻止狗头起飞 MC 模组工作区

这个仓库用于维护 Minecraft `1.21.11` 的地图配套 Fabric 模组。根目录是工作区，不再直接放某一个模组的源码；每个模组都放在 `mods/` 下的独立目录里。

## 目录

```text
mods/
  flyable/        生存模式飞行粘液球
  perseverance/   起飞超人 Boss 与毅力之证
```

根目录保留 Gradle Wrapper：

```text
gradlew
gradlew.bat
gradle/wrapper/
```

## 环境

- Minecraft `1.21.11`
- Fabric Loader `0.19.3`
- Fabric API `0.141.6+1.21.11` 或兼容版本
- JDK 21

本机可使用 Minecraft 自带 Java：

```powershell
$env:JAVA_HOME='C:\Users\ASUS\AppData\Roaming\.minecraft\runtime\java-runtime-delta'
```

## 构建

在仓库根目录构建全部模组：

```powershell
.\gradlew.bat build
```

单独构建某个模组：

```powershell
.\gradlew.bat buildFlyable
.\gradlew.bat buildPerseverance
```

也可以进入模组目录构建：

```powershell
cd mods\flyable
..\..\gradlew.bat build
```

## 产物

```text
mods/flyable/build/libs/flyable-1.0.0.jar
mods/perseverance/build/libs/perseverance-1.0.0.jar
```

这两个 jar 都不内置 Fabric API。使用时需要把 Fabric API 单独放进客户端或服务端的 `mods` 文件夹。

## 模组

`flyable`：

- `/give @p flyable:flight_slime_ball`
- `/give @p flyable:temporary_flight_slime_ball`
- 普通飞行粘液球可切换无限高速飞行。
- 限时飞行粘液球可飞行最多 10 秒，结束后进入 30 秒冷却。

`perseverance`：

- `/give @p perseverance:red_nether_star`
- `/give @p perseverance:perseverance_proof`
- 红色下界之星右键地面召唤 Boss“起飞超人”。
- 起飞超人血量 1000，攻击伤害 5，每秒攻击一次，攻击距离 4 格，显示 Boss 血条。
- 起飞超人使用 TheGou_ 皮肤，身穿钻套并手持钻剑作为装饰，死亡掉落“毅力之证”。
