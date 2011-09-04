<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<IEnumerable<CS631.Data.Employee>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section" > 
            <h1>List Of Employees</h1>
            <p />

            <div>
                <% using (Html.BeginForm(FormMethod.Get))
                   { %>
                        <table class="filter" >
                            <tr>
                                <td class="noborder"> 
                                    Department: <br />    
                                    <%: Html.DropDownList("DeptID", ViewBag.departments as SelectList, "")%>
                                </td>
                                <td class="noborder">&nbsp;</td>
                                <td class="noborder"> 
                                    Division: <br />    
                                    <%: Html.DropDownList("DivID", ViewBag.divisions as SelectList, "")%>
                                </td>
                                <td class="noborder">&nbsp;</td>
                                <td class="noborder"> 
                                    Office: <br />    
                                    <%: Html.DropDownList("OfficeID", ViewBag.offices as SelectList, "")%>
                                </td>
                                <td class="noborder">&nbsp;</td>
                                <td class="noborder" style="width:30px;" valign="bottom">
                                    &nbsp;<br />
                                    <input type="submit" value="Search" class="jqui_button_edit1" style="width:50px;"/>
                                </td>
                            </tr>
                        </table>
                       
                <% } %>
            </div>

            <table width="100%">
                <thead>
                    <tr>
                        <th> Empoyee No </th>
                        <th> First Name </th>
                        <th> Last Name </th>
                        <th> Actions </th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var e in Model)  { %>
                        <tr>
                            <td><%: e.EmployeeNO %> </td>
                            <td><%: e.EmpFName %> </td>
                            <td><%: e.EmpLName %> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", new { id = e.EmpID }, new { @class = "jqui_button_show", style = "padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", new { id = e.EmpID }, new { @class = "jqui_button_edit" })%> 
                                <%: Html.ActionLink("Delete", "Delete", new { id = e.EmpID }, new { @class = "jqui_button_delete" })%>
                            </td>

                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
     <div class="clear"></div> 

</asp:Content>
