function addAchievement() {
    const input = `<textarea class=\"info\" name=\"ACHIEVEMENTS\"></textarea> 
    <button id=\"add_achievement\" class=\"inner-button\" onclick = \"addAchievement()\">Add achievement</button>`;
    document.getElementById('add_achievement').outerHTML = input;
}

function resetAchievements() {
    const reset =
        `<textarea class=\"info\" name=\"ACHIEVEMENTS\"></textarea>
    <button id=\"add_achievement\" class=\"inner-button\" onclick=\"addAchievement()\">Add achievement</button>
    <button id=\"reset_achievements\" class=\"inner-button\" onclick=\"resetAchievements()\">Reset achievements</button>`;
    document.getElementById("ACHIEVEMENTS").innerHTML = reset;
}

function addQualification() {
    const input =
        `<textarea class=\"info\" name=\"QUALIFICATION\"></textarea>
    <button id=\"add_qualification\" class=\"inner-button\" onclick=\"addQualification()\">Add qualification</button>`;
    document.getElementById('add_qualification').outerHTML = input;
}

function resetQualifications() {
    const reset =
        `<textarea class=\"info\" name=\"QUALIFICATION\"></textarea>
    <button id=\"add_qualification\" class=\"inner-button\" onclick=\"addQualification()\">Add qualification</button>
    <button id=\"reset_qualifications\" class=\"inner-button\" onclick=\"resetQualifications()\">Reset Qualifications</button>`;
    document.getElementById("QUALIFICATION").innerHTML = reset;
}

function replaceInnerNames(old_element, new_element) {
    if (document.getElementById(`org-name-${new_element}`) == null) {
        let old_className = old_element;
        let org_id = "org-" + old_className;
        let classNames = document.getElementsByClassName(org_id);
        let btns = document.getElementsByName(`${old_className}`);

        document.getElementById(`org-name-${old_className}`).id = `org-name-${new_element}`;
        document.getElementById(`org-name-${new_element}`).className = new_element;
        document.getElementById(`positions-${old_className}`).id = `positions-${new_element}`;
        document.getElementById(`end-${old_element}`).id = `end-${new_element}`;

        //alert(classNames.length);
        //change names of position fields
        for (let i = 0; i < classNames.length; i++) {
            classNames[i].name = new_element;
        }
        //change classNames of position fields
        let names = document.getElementsByName(new_element);
        //alert(names.length);
        for (let i = 0; i < names.length; i++) {
            names[i].className = `org-${new_element}`;
        }
        //alert(btns.length);
        for (let i = 0; i < btns.length; i++) {
            btns[i].classList.add(`btn-${new_element}`);
            btns[i].classList.remove(`btn-${old_element}`);
        }
        let btns_names = document.getElementsByClassName(`btn-${new_element}`);
        for (let i = 0; i < btns_names.length; i++) {
            btns_names[i].name = `${new_element}`;
        }
    } else {
        alert("Org already exist! Choose another name.");
    }
}

function addPosition(element_name) {
    let position =
        `<div class="position-title-date">
            <div class="position-row">
                <p>Title: </p>
                <input class="org-${element_name}" type="text" name="${element_name}" value="" required>
            </div>
            <div class="position-row">
                <p>Start date: </p>
                <input class="org-${element_name}" type="date" name="${element_name}" value="" required>
            </div>
            <div class="position-row">
                <p>End date: </p>
                <input class="org-${element_name}" type="date" name="${element_name}" value="" required>
            </div>
        </div>
        <div class="description">
            <p style="text-align: center;">Description:</p> 
            <textarea class="org-${element_name}" name="${element_name}"></textarea>
        </div>
        <div id="end-${element_name}"></div>`
    document.getElementById(`end-${element_name}`).outerHTML = position;
}

function clearPositions(element_name) {
    document.getElementById(`positions-${element_name}`).innerHTML = `<div id="end-${element_name}"></div>`;
}

function addOrganization(id_value) {
    let id_count = +document.getElementById(`new-org-block-${id_value}`).getAttribute('id-value');
    id_count++;
    let org_value = id_value + id_count;
    //alert('org value is ' + org_value);
    document.getElementById(`new-org-block-${id_value}`).outerHTML =
        `<div id="org-block-${org_value}" class="org-block">
            <div class="org-name-url">
                <div class="org-row">
                    <p>Company name</p>
                     <input id="org-name-DEFAULT-${org_value}-ORG" class="DEFAULT-${org_value}-ORG" type="text" name="${id_value}" value=""
                            oninput="this.setAttribute('value', value)"
                            onchange="replaceInnerNames(this.className, this.value)">
                </div>
                <div class="org-row">
                    <p>Company URL (if exists)</p>
                    <input type="text" name="${id_value}" value="">
                </div>
            </div>
            <div id="positions-DEFAULT-${org_value}-ORG">
                <div id="end-DEFAULT-${org_value}-ORG"></div>
            </div>
            <div class="buttons">
                <input type="button" value="Add position" class="inner-button btn-DEFAULT-${org_value}-ORG" name="DEFAULT-${org_value}-ORG"
                    onclick="addPosition(this.name)">
                <input type="button" value="Remove positions" class="inner-button btn-DEFAULT-${org_value}-ORG" name="DEFAULT-${org_value}-ORG"
                    onclick="clearPositions(this.name)">
            </div>
        </div>
        <div id="new-org-block-${id_value}" id-value="${id_count}"></div>`;
}

function resetOrganizations(id_value) {
    let name_block = id_value[0] + id_value.substring(1).toLowerCase();
    document.getElementById(`${id_value}-block`).innerHTML =
    `<p>${name_block}</p>
    <div id="new-org-block-${id_value}" id-value="0"></div>
        <input id="${id_value}" class="inner-button" type="button" value="Add organization" onclick="addOrganization(this.id)"/>
        <input id="${id_value}" class="inner-button" type="button" value="Reset organizations" onclick="resetOrganizations(this.id)"/>`;
}


