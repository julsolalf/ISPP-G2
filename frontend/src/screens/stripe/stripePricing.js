import React, { useEffect } from "react";
 
 function StripePricing() {
      useEffect(() => { const script = document.createElement("script"); 
     script.src = "https://js.stripe.com/v3/pricing-table.js"; 
     script.async = true; document.body.appendChild(script);
 
 }, []);
 
 return ( <div style={{ margin: "20px" }}> <stripe-pricing-table pricing-table-id="prctbl_1RBJvo06ieomVv3RnXEeo4C1" publishable-key="pk_test_51RBHeY06ieomVv3R1luOa3x4v6Zs6JhjkgRjwtceCm1jVspTKomAmyeWZXY6ReXclE7CWLAy7VMeyUo2MShAH3VW00x6OEqyem"> </stripe-pricing-table> </div> ); }
 export default StripePricing;