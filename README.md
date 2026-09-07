# splitbill
For Backend Tech Test

---
The hardest part for me was deciding how I wanted to design the API at the beginning, especially the payload, response, and the flow for the settlement calculation.

I actually spent quite a lot of time thinking about it because I wanted to find a good way to structure everything before I started coding. But once I started implementing it, I realized that some parts of my initial design were probably more complicated than they needed to be. There were a few moments where I thought, “I think I could have designed this part better.”

The trade-off was that I decided to go with the design I had at that point so I could move forward and get the whole flow working first. If I had more time, I would definitely go back and simplify some of the flow and refactor the parts that feel less efficient.
--- 

Payload Request Example :

{
    "title": "Lunch",
    "all_participant": [
        "umi",
        "selda",
        "justin",
        "selena",
        "bieber",
        "olivia",
        "rodrigo"
    ],
    "detail": [
        {
            "item": "cumi",
            "paid_by": "selda",
            "price": "100000.00",
            "category": "food",
            "participant": [
                "umi",
                "selda",
                "justin",
                "selena"
            ]
        },
        {
            "item": "ikan",
            "paid_by": "selena",
            "price": "90000.00",
            "category": "food",
            "participant": [
                "umi",
                "selda",
                "selena"
            ]
        },
        {
            "item": "bakwan",
            "paid_by": "selda",
            "price": "6.00",
            "category": "food",
            "participant": [
                "umi",
                "selda"
            ]
        }
    ]
}

