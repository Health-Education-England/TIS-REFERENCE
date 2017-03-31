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
import {FundingTypeDetailComponent} from "../../../../../../main/webapp/app/entities/funding-type/funding-type-detail.component";
import {FundingTypeService} from "../../../../../../main/webapp/app/entities/funding-type/funding-type.service";
import {FundingType} from "../../../../../../main/webapp/app/entities/funding-type/funding-type.model";

describe('Component Tests', () => {

	describe('FundingType Management Detail Component', () => {
		let comp: FundingTypeDetailComponent;
		let fixture: ComponentFixture<FundingTypeDetailComponent>;
		let service: FundingTypeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [FundingTypeDetailComponent],
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
					FundingTypeService
				]
			}).overrideComponent(FundingTypeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(FundingTypeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(FundingTypeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new FundingType(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.fundingType).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
