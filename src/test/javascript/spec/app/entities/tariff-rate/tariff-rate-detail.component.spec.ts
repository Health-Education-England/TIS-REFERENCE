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
import {TariffRateDetailComponent} from "../../../../../../main/webapp/app/entities/tariff-rate/tariff-rate-detail.component";
import {TariffRateService} from "../../../../../../main/webapp/app/entities/tariff-rate/tariff-rate.service";
import {TariffRate} from "../../../../../../main/webapp/app/entities/tariff-rate/tariff-rate.model";

describe('Component Tests', () => {

	describe('TariffRate Management Detail Component', () => {
		let comp: TariffRateDetailComponent;
		let fixture: ComponentFixture<TariffRateDetailComponent>;
		let service: TariffRateService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [TariffRateDetailComponent],
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
					TariffRateService
				]
			}).overrideComponent(TariffRateDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(TariffRateDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(TariffRateService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new TariffRate(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.tariffRate).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
