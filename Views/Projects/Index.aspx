<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<IEnumerable<CS631.Data.Project>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">

        <div class="section" > 
            <h1>List Of Projects</h1>
            <p />
            <table width="100%">
                <thead>
                    <tr>
                        <th>Project NO</th>
                        <th>Name</th>
                        <th>Budget</th>
                        <th>Date Started</th>
                        <th>Date Ended</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var p in Model)  { %>
                        <tr>
                            <td><%: p.ProjectNO %> </td>
                            <td><%: p.ProjName %> </td>
                            <td><%: p.ProjBudget %> </td>
                            <td><%: p.StartDate.ToString("MM/dd/yyyy") %> </td>
                            <td><%: p.EndDate %> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", new { id = p.ProjID }, new { @class="jqui_button_show", style="padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", new { id = p.ProjID }, new { @class = "jqui_button_edit" })%> 
                                <%: Html.ActionLink("Delete", "Delete", new { id = p.ProjID }, new { @class = "jqui_button_delete" })%>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
     <div class="clear"></div> 

</asp:Content>
