<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Division>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Departments
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">


    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">

        <div class="section" > 
            <h1>Departments</h1>
            <p>List Of Departments in division <%: Model.DivName %> </p>
            <table width="100%">
                <thead>
                    <tr>
                        <th> Department No </th>
                        <th> Name </th>
                        <th> Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var d in Model.Departments())  { %>
                        <tr>
                            <td><%: d.DepartmentNo %> </td>
                            <td><%: d.DeptName %> </td>

                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", "Departments", new { id = d.DeptID }, new { @class = "jqui_button_show", style = "padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", "Departments", new { id = d.DeptID }, new { @class = "jqui_button_edit" })%> 
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
    <div class="clear"></div> 

</asp:Content>
