<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<IEnumerable<CS631.Data.Milestone>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">

        <div class="section" > 
            <h1>List Of Milestones</h1>
            <p />
            <table width="100%">
                <thead>
                    <tr>
                        <th>Milestone NO</th>
                        <th>Project NO</th>
                        <th>Date</th>
                        <th>Deliverable</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var p in Model)  { %>
                        <tr>
                            <td><%: p.MilestoneID %> </td>
                            <td><%: p.ProjID %> </td>
                            <td><%: p.MilestonePlannedDate.ToString("MM/dd/yyyy") %> </td>
                            <td><%: p.MilestoneDeliverable%> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", new { id = p.MilestoneID }, new { @class = "jqui_button_show", style = "padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", new { id = p.MilestoneID }, new { @class = "jqui_button_edit" })%> 
                                <%: Html.ActionLink("Delete", "Delete", new { id = p.MilestoneID }, new { @class = "jqui_button_delete" })%>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
     <div class="clear"></div> 

</asp:Content>

