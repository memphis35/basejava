function addAchievements() {
    const input = `<textarea class=\"info\" name=\"ACHIEVEMENTS\"></textarea> 
    <input id=\"add_achievements\" class=\"info_button\" type=\"button\" value=\"Add field\" onclick = \"addAchievements()\">`;
    document.getElementById('add_achievements').outerHTML = input;
}

function resetAchievements() {
    const reset =
        `<textarea class=\"info\" name=\"ACHIEVEMENTS\"></textarea>
    <input id=\"add_achievements\" class=\"info_button\" type=\"button\" value=\"Add field\" onclick=\"addAchievements()\">
    <input id=\"reset_achievements\" class=\"info_button\" type=\"button\" value=\"Reset fields\" onclick=\"resetAchievements()\">`;
    document.getElementById("ACHIEVEMENTS").innerHTML = reset;
}

function addQualifications() {
    const input =
        `<textarea class=\"info\" name=\"QUALIFICATION\"></textarea>
    <input id=\"add_qualifications\" class=\"info_button\" type=\"button\" value=\"Add field\" onclick=\"addQualifications()\">`;
    document.getElementById('add_qualifications').outerHTML = input;
}

function resetQualifications() {
    const reset =
        `<textarea class=\"info\" name=\"QUALIFICATION\"></textarea>
    <input class=\"info_button\" type=\"button\" value=\"Add field\" id=\"add_qualifications\" onclick=\"addQualifications()\">
    <input class=\"info_button\" type=\"button\" value=\"Reset fields" id=\"reset_qualifications\" onclick=\"resetQualifications()\">`;
    document.getElementById("QUALIFICATION").innerHTML = reset;
}

