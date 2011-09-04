<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Milestone>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Details
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">


    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>


    <div class="grid_10">
        <div class="section"> 
            <h1>Show Milestone</h1>
            <p></p>

            <table class="details">
                <tr>
                    <th><%: Html.LabelFor(model => model.ProjID) %></th>
                    <td>
                        <%: Model.ProjID %>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => model.MilestonePlannedDate)%></th>
                    <td>
                        <%: Model.MilestonePlannedDate %>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.MilestoneDeliverable)%></th>
                    <td>
                        <%: Model.MilestoneDeliverable %>
                    </td>
                </tr>

                <tr>
                    <th> <%: Html.LabelFor(model => model.ToBeDelivered)%></th>
                    <td>
                        <%: Model.ToBeDelivered %>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.MilestoneDeliveryDate)%></th>
                    <td>
                        <%: Model.MilestoneDeliveryDate %>
                    </td>
                </tr>

            </table>
            <div class="clear"></div> 

        </div>
    </div>
    <div class="clear"></div> 


</asp:Content>
