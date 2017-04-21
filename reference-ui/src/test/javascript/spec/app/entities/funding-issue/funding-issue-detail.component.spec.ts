import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {FundingIssueDetailComponent} from "../../../../../../main/webapp/app/entities/funding-issue/funding-issue-detail.component";
import {FundingIssueService} from "../../../../../../main/webapp/app/entities/funding-issue/funding-issue.service";
import {FundingIssue} from "../../../../../../main/webapp/app/entities/funding-issue/funding-issue.model";

describe('Component Tests', () => {

	describe('FundingIssue Management Detail Component', () => {
		let comp: FundingIssueDetailComponent;
		let fixture: ComponentFixture<FundingIssueDetailComponent>;
		let service: FundingIssueService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [FundingIssueDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					FundingIssueService
				]
			}).overrideComponent(FundingIssueDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(FundingIssueDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(FundingIssueService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new FundingIssue(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.fundingIssue).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
