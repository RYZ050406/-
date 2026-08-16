# perseverance Fabric 模组

Minecraft `1.21.11` 的 Fabric 模组，新增红色下界之星召唤物和 Boss 敌对生物“起飞超人”。

## 使用

构建：

```powershell
$env:JAVA_HOME='C:\Users\ASUS\AppData\Roaming\.minecraft\runtime\java-runtime-delta'
..\gradlew.bat build
```

获取召唤物：

```mcfunction
/give @p perseverance:red_nether_star
```

获取掉落物：

```mcfunction
/give @p perseverance:perseverance_proof
```

右键地面使用红色下界之星会召唤 Boss“起飞超人”。起飞超人血量 1000，攻击伤害 5，每秒攻击一次，攻击距离 4 格，显示 Boss 血条，身穿钻套并手持钻剑作为装饰。
