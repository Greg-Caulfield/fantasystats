# FantasyStats

Pulls week-by-week scoring data using the ESPN APIs to calculate the win/loss record of each player. 

Currently outputs a comma-separated string of the team ids, wins, losses, and points for. This gets copied manually into an google sheet to be shared.

### Future TODOs
<ul>
<li>Order the output of teams automatically based on wins then points for.</li>
<li>Refactor to more than one big method.</li>
<li>Account for ties.</li>
<li><strike>Move model objects into repositories.</strike></li>
<li>Expose endpoints to view stats on a per team basis.</li>
</ul>  

### Long Term Goals
<ul>
<li>Build corresponding frontend application using Vue.js.</li>
<li>Host application on cloud provided servers.</li>
</ul>