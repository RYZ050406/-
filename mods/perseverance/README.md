# perseverance Fabric 模组

Minecraft `1.21.11` 的 Fabric 模组，新增红色下界之星召唤物和 Boss 敌对生物“起飞超人”。

## 使用

需要：

- JDK 21
- 游戏客户端或服务端需要安装 Fabric API `0.141.6+1.21.11` 或兼容版本

从仓库根目录构建：

```powershell
.\gradlew.bat buildPerseverance
```

或进入本模组目录构建：

```powershell
cd mods\perseverance
..\..\gradlew.bat build
```

构建完成后，把 `mods/perseverance/build/libs/perseverance-1.0.0.jar` 放进客户端或服务端的 `mods` 文件夹。

这个构建不会内置 Fabric API，需要在 `mods` 文件夹里单独放 Fabric API。

获取召唤物：

```mcfunction
/give @p perseverance:red_nether_star
```

获取掉落物：

```mcfunction
/give @p perseverance:perseverance_proof
```

右键地面使用红色下界之星会召唤 Boss“起飞超人”。起飞超人血量 1000，攻击伤害 5，每秒攻击一次，攻击距离 4 格，显示 Boss 血条，身穿钻套并手持钻剑作为装饰。
