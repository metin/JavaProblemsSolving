<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<IEnumerable<CS631.Data.Department>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section" > 
            <h1>List Of Departments</h1>
            <p />
            <table width="100%">
                <thead>
                    <tr>
                        <th> Department No </th>
                        <th> Name </th>
                        <th> Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var d in Model) { %>
                        <tr>
                            <td><%: d.DepartmentNo %> </td>
                            <td><%: d.DeptName %> </td>

                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", new { id = d.DeptID }, new { @class="jqui_button_show", style="padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", new { id = d.DeptID }, new { @class = "jqui_button_edit" })%> 
                                <%: Html.ActionLink("Delete", "Delete", new { id = d.DeptID }, new { @class = "jqui_button_delete" })%>
                            </td>

                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
     <div class="clear"></div> 

</asp:Content>
