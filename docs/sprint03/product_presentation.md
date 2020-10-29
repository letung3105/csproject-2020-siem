---
marp: true
---

# A CEP-based SIEM System (Jupiter)

+ Bui Xuan Phuoc
+ Dang Chi Cong
+ Pham Nguyen Thanh An
+ Vo Le Tung

---

# Overview: Security information and event managment

+ Collect and aggregate data:
    + Data comes from multiple sources.
    + Normalise the data.
+ Provide analysis and reporting of data.
+ Monitor:
    + Cross correlation.
    + Provide notification.

---

# Overview: Complex event processing

+ Events stream processing
+ Infer *complex* events based on other events
    + Pattern detection
    + Filter
    + Aggregation
    + etc.

---

# Overview: Complex event processing

**E.g.:**

1. Puts hands to mouth.
2. Turns head towards mom’s breast or bottle.
3. Puckers, smacks, or licks lips.
3. Has clenched hands.

=> Baby is hungry

[(https://www.cdc.gov/nutrition/InfantandToddlerNutrition/mealtime/signs-your-child-is-hungry-or-full.html)](https://www.cdc.gov/nutrition/InfantandToddlerNutrition/mealtime/signs-your-child-is-hungry-or-full.html)

---

# Our SIEM system

![](images/SIEMArchitecture.png)

---

# Our SIEM system: Port scan detection