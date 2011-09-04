<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<CS631.Data.ProjectMember>" %>

<% var p = ViewBag.project as CS631.Data.Project; %>

<% using (Html.BeginForm("Members", "Projects", FormMethod.Post)) { %>
    <%: Html.ValidationSummary(true) %>


    <%: Html.Hidden("ProjID", p.ProjID)%>

    <table class="details">

        <tr>
            <th><%: Html.LabelFor(model => model.EmpID) %></th>
            <td>
                <%: Html.DropDownListFor(model => model.EmpID, ViewBag.employees as SelectList, "")%>
                <%: Html.ValidationMessageFor(model => model.EmpID)%>
            </td>
        </tr>


        <tr>
            <th><%: Html.LabelFor(model => model.Role) %></th>
            <td>
                <%: Html.DropDownListFor(model => model.Role, ViewBag.roles as SelectList, "")%>
                <%: Html.ValidationMessageFor(model => model.Role)%>
            </td>
        </tr>

    </table>

    <div class="clear"></div> 
    <div>
        <input type="submit" value="Save" class="action_button"/>
    </div>
    <div class="clear"></div> 


<% } %>
