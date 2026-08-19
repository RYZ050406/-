# Boss

Minecraft `1.21.11` Fabric 模组，增加六把技能剑、两双强化靴子和一个 BOSS 模式下界之星。

## 构建

```powershell
..\..\gradlew.bat build
```

产物：

```text
build/libs/boss-1.0.0.jar
```

## 道具

- `/give @p boss:sword_one`
- `/give @p boss:sword_two`
- `/give @p boss:sword_three`
- `/give @p boss:sword_four`
- `/give @p boss:sword_five`
- `/give @p boss:dog_king_sword`
- `/give @p boss:leaping_leather_boots`
- `/give @p boss:god_netherite_boots`
- `/give @p boss:boss_mode_star`

## 狗王剑

- 名称为棕色加粗斜体“狗王剑”。
- 不可破坏，基础伤害 10，主手持有时移动速度 +0.05。
- 右键射出 40 格激光，命中活体造成 20 点普通玩家攻击伤害，可被护甲和抗性削减。
- 每把狗王剑独立记录 30 秒激光 CD，手持时显示冷却或“激光已就绪”。