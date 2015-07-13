Possible Patterns
=================

###Pattern 1###
**Name:** Missing constraint from inherited Clafer

**Example:**
```clafer
abstract Location
abstract Edge
  location -> Location 2
  length ->> int

abstract HarnessEdge : Edge
  area ->> int

Harness
  l1 : Location
  l2 : Location
  l3 : Location

  e1 : HarnessEdge
    [length = 1]
    [area = 2]
  e2 : HarnessEdge
    [location = (l2, l3)]
    [length = 1]
    [area = 3]
```
The problem with this example is that e1 can take any location that is defined which is a mistake. This could be a big problem if the model is huge as it will exponentially increase the instance number. Even worse is the fact that when doing optimization it could give a solution that is not valid for our modeled system.

**Possible Solution:** Using likely invariant approach we could tell with some sort of confidence whether the user intended to leave out the constraint or not.

###Pattern 2###
**Name:** Missing ".ref" of Clafer

**Example:**
```clafer
abstract Command
    sender -> FunctionalAnalysisComponent
    receiver -> FunctionalAnalysisComponent
    deployedTo -> LogicalDataConnector ?
        [parent in this.deployedFrom]
    [(sender.deployedTo = receiver.deployedTo) <=> no this.deployedTo]
```

The problem in this example is that when we compare sender.deployedTo to receiver.deployedTo we are only compairing if sender.deployedTo exists and receiver.deployedTo exists as well. What we really want is the value that these references point to. This is very similar to mistakenly not dereferencing a pointer in C/C++. Thus what we want the example to look like is:
```clafer
abstract Command
    sender -> FunctionalAnalysisComponent
    receiver -> FunctionalAnalysisComponent
    deployedTo -> LogicalDataConnector ?
        [parent in this.deployedFrom]
    [(sender.deployedTo.ref = receiver.deployedTo.ref) <=> no this.deployedTo]
```
This is a serious problem as well because again the model will compile correctly and the user will retrieve instances but it is very likely the generated instances are incorrect since the model is not what the user intended.

**Possible Solution:** For this pattern I would not use the likely invariant approach since almost always when the user performs this type of comparison between two references with an "=" they are intending to compare the values. I might consider using likely invariant if it is effective in finding this mistake.

###Pattern 3###
**Name:** Missing implies on variability

**Example:**
```clafer
[fa.PinchDetectionFA.PositionSensor.deployedTo.ref = ht.dn.Motor]
```
The problem with this example is that PinchDetectionFA is an optional Clafer that is constrained by some other Clafer in the model. Now what is trying to be modeled is that a constraint needs to be applied to the nested PositionSensor ***if*** the PinchDetectionFA is present in the instance. Sometimes the solver will not be smart enough to handle this case so we should put an implies constraint to be more explicit as follows:

```clafer
[fa.PinchDetectionFA => (fa.PinchDetectionFA.PositionSensor.deployedTo.ref = ht.dn.Motor)]
```

**Possible Solution:** For this pattern I would do a pattern match for an optional Clafer that has nested Clafers that are being constrained. If this pattern exists I would throw a warning for possibility losing variability in the model.

###Pattern 4###
**Name:** Incorrect use of "=", should use "in"

**Example:**
```clafer
abstract FunctionalAnalysisComponent
  deployedTo -> DeviceNode
    [parent in this.deployedFrom]

abstract DeviceNode
  deployedFrom -> FunctionalAnalysisComponent *
    [this.deployedTo = parent]

function1 : FunctionalAnalysisComponent
  [deployedTo = (device1, device2)]

device1 : DeviceNode
device2 : DeviceNode
```
This example is incorrect because we should be using `[deployedTo in (device1, device2)]`. This is problem is a hassle for users when they can't find why they are getting 0 instances and an even bigger problem when the model generates spurious instances for different forms of this problem.

**Possible Solution:** For this pattern I would do a pattern match on `<Singleton Set (or optional)> = <Non-singleton Set>`. If this pattern exists I would throw a warning for incorrect use of "=" with a singleton set and a non-singleton set.
