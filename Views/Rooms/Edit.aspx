<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Room>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Edit
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">

        <div class="section"> 
            <h1>Edit Room</h1>
            <p>Editing room information </p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                <%: Html.HiddenFor(model => model.Id) %>
                <table class="details">
                    <tr>
                        <th> <%: Html.LabelFor(model => model.Code)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.Code) %>
                            <%: Html.ValidationMessageFor(model => model.Code)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.BuildingId)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.BuildingId, ViewBag.buildings as SelectList)%>
                            <%: Html.ValidationMessageFor(model => model.BuildingId)%>
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
