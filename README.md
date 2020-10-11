#### How to compile ?
- Need to install `sbt`
- `cd` to root directory
- `sbt clean compile`

#### How to run tests ?
- `sbt clean test`

#### Deposit
**How to it work ?**
1. You can choose between two deposit plan or both
    - One Time Plan
    - Monthly Plan
2. In plan you can choose your fund distribution whenever money is deposited
3. **Monthly plan** is for recurring deposits
4. wallet can store your money if not sufficient deposit is made and it can be used in plan if wallet amount reaches to allocated plan.
5. One time plan is given priority over monthly if both plans are defined for a customer.
6. For running at local added in memory data repositry.


 **How to run ?**
`sbt "runMain com.stashorg.RunApplication"`

**Assumptions**
- wallet for customer is used if user send more or less monthly than allocated plan.

