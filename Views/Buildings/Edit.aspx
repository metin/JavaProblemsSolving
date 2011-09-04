<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Building>" %>

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
            <h1>Edit Building</h1>
            <p>Editing bulding information </p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                <%: Html.HiddenFor(model => model.BuildingID) %>
                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.BuildingName)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.BuildingName)%>
                            <%: Html.ValidationMessageFor(model => model.BuildingName)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.BuildingCode)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.BuildingCode)%>
                            <%: Html.ValidationMessageFor(model => model.BuildingCode)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.YearAcquired)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.YearAcquired)%>
                            <%: Html.ValidationMessageFor(model => model.YearAcquired)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.BuildingCost)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.BuildingCost)%>
                            <%: Html.ValidationMessageFor(model => model.BuildingCost)%>
                        </td>
                    </tr>
                </table>
                <div class="clear"></div> 
                <div>
                    <input type="submit" value="Save" class="action_button"/>
                </div>
                <div class="clear"></div>
            <% } %>
        </div>
    </div>
    <div class="clear"></div> 



</asp:Content>

