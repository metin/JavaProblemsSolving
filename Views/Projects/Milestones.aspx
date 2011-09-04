<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Project>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Milestones
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section">

            <h1>Project Milestones</h1>
            <p></p>
            <table width="100%">
                <thead>
                    <tr>
                        <th>Milestone NO</th>
                        <th>Date</th>
                        <th>Deliverable</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var p in Model.Milestones())  { %>
                        <tr>
                            <td><%: p.MilestoneID %> </td>
                            <td><%: p.MilestonePlannedDate.ToString("MM/dd/yyyy") %> </td>
                            <td><%: p.MilestoneDeliverable%> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", "Milestones", new { id = p.MilestoneID }, new { @class = "jqui_button_show", style = "padding: 0px;" })%> 
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>


        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>
