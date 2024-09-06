\* Gaming Bar Setup ILP *\
\* Integer Variables: Arcade_Machines, VR_Stations *\

Maximize \* Entertainment Value *\
    80 Arcade_Machines + 90 VR_Stations

\* Positive Amounts only *\
\* Stay within budget *\
\* Adhere to limited space *\
Subject To
    Arcade_Machines >= 0
    VR_Stations >= 0
    200 Arcade_Machines + 500 VR_Stations <= 4000
    5 Arcade_Machines + 3 VR_Stations <= 50

Integer \* We must nt have fractional Hardware *\
    Arcade_Machines
    VR_Stations

End

\* Solve with cbc gamingbar.lp solve solution solution.txt *\