<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Employee>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Create
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section"> 
            <h1>New Employee</h1>
            <p>Add new employee</p>
            <br />

             <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>


                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpFName) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.EmpFName) %>
                            <%: Html.ValidationMessageFor(model => model.EmpFName) %>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpMI)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.EmpMI)%>
                            <%: Html.ValidationMessageFor(model => model.EmpMI)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpLName) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.EmpLName) %>
                            <%: Html.ValidationMessageFor(model => model.EmpLName) %>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpTitle)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.EmpTitle)%>
                            <%: Html.ValidationMessageFor(model => model.EmpTitle)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpBuilding)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.EmpBuilding, ViewBag.buildings as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.EmpBuilding)%>
                        </td>
                    </tr>


                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpOffice)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.EmpOffice, ViewBag.offices as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.EmpOffice)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpPhone)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.EmpPhone)%>
                            <%: Html.ValidationMessageFor(model => model.EmpPhone)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpDept)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.EmpDept, ViewBag.departments as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.EmpDept)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpDiv)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.EmpDiv, ViewBag.divisions as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.EmpDiv)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpType)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.EmpType, ViewBag.employment as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.EmpType)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.HourRate)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.HourRate)%>
                            <%: Html.ValidationMessageFor(model => model.HourRate)%>
                        </td>
                    </tr>

                </table>

                <div class="clear"></div> 
                <div>
                    <input type="submit" value="Create" class="action_button"/>
                </div>
                <div class="clear"></div>
            <% } %>
        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>

